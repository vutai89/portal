package com.mcredit.sharedbiz.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.ADUser;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class LDAPService {
	
	private DirContext dirContext = null;
	private String _host;
	private String _port;
	private String _rootdn;
	private String _username;
	private String _password;
	/**
	 * Constructor for the LdapClient object.
	 * @throws NamingException 
	 */
	public LDAPService(){
		this._host = CacheManager.Parameters().findParamValueAsString(ParametersName.AD_HOST);
		this._port = CacheManager.Parameters().findParamValueAsString(ParametersName.AD_PORT);
		this._rootdn = CacheManager.Parameters().findParamValueAsString(ParametersName.AD_ROOTDN);
		System.out.println("AD Infomation: " + this._host + this._port + this._rootdn);
	}

	/**
	 * Connect to server.
	 */
	public void connect(String username, String password) throws NamingException {
		this._username = username;
		this._password = password;
		dirContext = new InitialDirContext(getConnectionSettings(username,password));
	}

	/**
	 * Disconnect from the server.
	 */
	public void disconnect() {
		try {
			if (dirContext != null) {
				dirContext.close();
				dirContext = null;
			}
		} catch (NamingException e) {
			
		}
	}
	
   
	public ArrayList<ADUser> getUser(String query) throws Exception {
		//String returnedAtts[] = { "distinguishedName", "sAMAccountName", "userPrincipalName", "displayName", "cn", "sn", "givenName", "mail", "department", "company", "manager", "telephoneNumber","password" };
		SearchControls searchContext = new SearchControls(SearchControls.SUBTREE_SCOPE,0,1000,null,false,false);
		
		ArrayList<ADUser> users = new ArrayList<ADUser>();
		
		LdapContext ctx = null;
		byte[] cookie = null;
		int pageSize = 1000;
		try {
			ctx = new InitialLdapContext(getConnectionSettings(this._username,this._password), null);
			ctx.setRequestControls(new Control[]{new PagedResultsControl(pageSize, Control.NONCRITICAL)});
			
			do {
			NamingEnumeration<SearchResult> results = ctx.search(this._rootdn,"(&(objectClass=person)" + query + ")", searchContext);
			while (results.hasMoreElements()) {
				SearchResult item = results.next(); 
				Attributes metadata = item.getAttributes();
				NamingEnumeration<String> attributes = metadata.getIDs();
				
				ArrayList<String> availableValues = new ArrayList<String>();
				while (attributes.hasMoreElements()) {
					availableValues.add(attributes.next());
				}
				
				ADUser u = new ADUser();
				u.commonName = availableValues.contains("cn") ? String.valueOf(metadata.get("cn").get()) : "";
				u.company =  availableValues.contains("company") ? String.valueOf(metadata.get("company").get()) : "";
				u.department = availableValues.contains("department") ? String.valueOf(metadata.get("department").get()) : "";
				u.dn = availableValues.contains("distinguishedName") ? deAccent(String.valueOf(metadata.get("distinguishedName").get())) : "";
				u.email = availableValues.contains("mail") ? String.valueOf(metadata.get("mail").get()) : "";
				u.familyName = availableValues.contains("sn") ? deAccent(String.valueOf(metadata.get("sn").get())) : "";
				u.givenName = availableValues.contains("givenName") ? deAccent(String.valueOf(metadata.get("givenName").get())) : "";
				u.manager = availableValues.contains("manager") ? String.valueOf(metadata.get("manager").get()) : "";
				u.phone = availableValues.contains("telephoneNumber") ? String.valueOf(metadata.get("telephoneNumber").get()) : "";
				u.userId = availableValues.contains("sAMAccountName") ? String.valueOf(metadata.get("sAMAccountName").get()) : "";
				u.name = availableValues.contains("name") ? String.valueOf(metadata.get("name").get()) : "";
				u.logonHours = availableValues.contains("logonHours") ? String.valueOf(metadata.get("logonHours").get()) : "";
				u.primaryGroupID = availableValues.contains("primaryGroupID") ? String.valueOf(metadata.get("primaryGroupID").get()) : "";
				u.objectClass = availableValues.contains("objectClass") ? String.valueOf(metadata.get("objectClass").get()) : "";
				u.adminCount = availableValues.contains("adminCount") ? String.valueOf(metadata.get("adminCount").get()) : "";
				u.badPasswordTime = availableValues.contains("badPasswordTime") ? toTimestamp(String.valueOf(metadata.get("badPasswordTime").get())) : "";
				u.objectCategory = availableValues.contains("objectCategory") ? String.valueOf(metadata.get("objectCategory").get()) : "";
				u.userAccountControl = availableValues.contains("userAccountControl") ? String.valueOf(metadata.get("userAccountControl").get()) : "";
				u.dSCorePropagationData = availableValues.contains("dSCorePropagationData") ? dateToStringFormat(String.valueOf(metadata.get("dSCorePropagationData").get())) : "";
				u.codePage = availableValues.contains("codePage") ? String.valueOf(metadata.get("codePage").get()) : "";
				u.whenChanged = availableValues.contains("whenChanged") ? dateToStringFormat(String.valueOf(metadata.get("whenChanged").get())) : "";
				u.whenCreated = availableValues.contains("whenCreated") ? dateToStringFormat(String.valueOf(metadata.get("whenCreated").get())) : "";
				u.pwdLastSet = availableValues.contains("pwdLastSet") ? toTimestamp(String.valueOf(metadata.get("pwdLastSet").get())) : "";
				u.logonCount = availableValues.contains("logonCount") ? String.valueOf(metadata.get("logonCount").get()) : "";
				u.isCriticalSystemObject = availableValues.contains("isCriticalSystemObject") ? String.valueOf(metadata.get("isCriticalSystemObject").get()) : "";
				u.description = availableValues.contains("description") ? deAccent(String.valueOf(metadata.get("description").get())) : "";
				u.accountExpires = availableValues.contains("pwdLastSet") ? convertExpiredDate(String.valueOf(metadata.get("pwdLastSet").get())) : ""; 
				u.lastLogoff = availableValues.contains("lastLogoff") ? String.valueOf(metadata.get("lastLogoff").get()) : "";
				u.lastLogonTimestamp = availableValues.contains("lastLogonTimestamp") ? toTimestamp(String.valueOf(metadata.get("lastLogonTimestamp").get())) : "";
				u.objectGUID = availableValues.contains("objectGUID") ? String.valueOf(metadata.get("objectGUID").get()) : "";
				u.lastLogon = availableValues.contains("lastLogon") ? toTimestamp(String.valueOf(metadata.get("lastLogon").get())) : "";
				u.uSNChanged = availableValues.contains("uSNChanged") ? String.valueOf(metadata.get("uSNChanged").get()) : "";
				u.uSNCreated = availableValues.contains("uSNCreated") ? String.valueOf(metadata.get("uSNCreated").get()) : "";
				u.countryCode = availableValues.contains("countryCode") ? String.valueOf(metadata.get("countryCode").get()) : "";
				u.instanceType = availableValues.contains("instanceType") ? String.valueOf(metadata.get("instanceType").get()) : "";
				u.memberOf = availableValues.contains("memberOf") ? String.valueOf(metadata.get("memberOf").get()) : "";
				u.badPwdCount = availableValues.contains("badPwdCount") ? String.valueOf(metadata.get("badPwdCount").get()) : "";
				u.objectSid = availableValues.contains("objectSid") ? String.valueOf(metadata.get("objectSid").get()) : "";

				users.add(u);
			}
			
			Control[] controls = ctx.getResponseControls();
            if (controls != null) {
                for (int i = 0; i < controls.length; i++) {
                    if (controls[i] instanceof PagedResultsResponseControl) {
                        PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                        cookie = prrc.getCookie();
                    }
                }
            }
            ctx.setRequestControls(new Control[]{new PagedResultsControl(pageSize, cookie, Control.CRITICAL)});
			}while (cookie != null);
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
		
		return users;
	}
	
	
	public ArrayList<ADUser> getAllUser() throws Exception {
		//String returnedAtts[] = { "distinguishedName", "sAMAccountName", "userPrincipalName", "displayName", "cn", "sn", "givenName", "mail", "department", "company", "manager", "telephoneNumber","password" };
		SearchControls searchContext = new SearchControls(SearchControls.SUBTREE_SCOPE,0,1000,null,false,false);
		
		ArrayList<ADUser> users = new ArrayList<ADUser>();
		
		LdapContext ctx = null;
		byte[] cookie = null;
		int pageSize = 1000;
		try {
			ctx = new InitialLdapContext(getConnectionSettings(this._username,this._password), null);
			ctx.setRequestControls(new Control[]{new PagedResultsControl(pageSize, Control.NONCRITICAL)});
			
			do {
			NamingEnumeration<SearchResult> results = ctx.search(this._rootdn,"(&(objectClass=person))", searchContext);
			while (results.hasMoreElements()) {
				SearchResult item = results.next(); 
				Attributes metadata = item.getAttributes();
				NamingEnumeration<String> attributes = metadata.getIDs();
				
				ArrayList<String> availableValues = new ArrayList<String>();
				while (attributes.hasMoreElements()) {
					availableValues.add(attributes.next());
				}
				
				ADUser u = new ADUser();
				u.commonName = availableValues.contains("cn") ? String.valueOf(metadata.get("cn").get()) : "";
				u.company =  availableValues.contains("company") ? String.valueOf(metadata.get("company").get()) : "";
				u.department = availableValues.contains("department") ? String.valueOf(metadata.get("department").get()) : "";
				u.dn = availableValues.contains("distinguishedName") ? deAccent(String.valueOf(metadata.get("distinguishedName").get())) : "";
				u.email = availableValues.contains("mail") ? String.valueOf(metadata.get("mail").get()) : "";
				u.familyName = availableValues.contains("sn") ? deAccent(String.valueOf(metadata.get("sn").get())) : "";
				u.givenName = availableValues.contains("givenName") ? deAccent(String.valueOf(metadata.get("givenName").get())) : "";
				u.manager = availableValues.contains("manager") ? String.valueOf(metadata.get("manager").get()) : "";
				u.phone = availableValues.contains("telephoneNumber") ? consolidatePhone(String.valueOf(metadata.get("telephoneNumber").get())) : "";
				u.userId = availableValues.contains("sAMAccountName") ? String.valueOf(metadata.get("sAMAccountName").get()) : "";
				u.name = availableValues.contains("name") ? String.valueOf(metadata.get("name").get()) : "";
				u.logonHours = availableValues.contains("logonHours") ? String.valueOf(metadata.get("logonHours").get()) : "";
				u.primaryGroupID = availableValues.contains("primaryGroupID") ? String.valueOf(metadata.get("primaryGroupID").get()) : "";
				u.objectClass = availableValues.contains("objectClass") ? String.valueOf(metadata.get("objectClass").get()) : "";
				u.adminCount = availableValues.contains("adminCount") ? String.valueOf(metadata.get("adminCount").get()) : "";
				u.badPasswordTime = availableValues.contains("badPasswordTime") ? toTimestamp(String.valueOf(metadata.get("badPasswordTime").get())) : "";
				u.objectCategory = availableValues.contains("objectCategory") ? String.valueOf(metadata.get("objectCategory").get()) : "";
				u.userAccountControl = availableValues.contains("userAccountControl") ? String.valueOf(metadata.get("userAccountControl").get()) : "";
				u.dSCorePropagationData = availableValues.contains("dSCorePropagationData") ? dateToStringFormat(String.valueOf(metadata.get("dSCorePropagationData").get())) : "";
				u.codePage = availableValues.contains("codePage") ? String.valueOf(metadata.get("codePage").get()) : "";
				u.whenChanged = availableValues.contains("whenChanged") ? dateToStringFormat(String.valueOf(metadata.get("whenChanged").get())) : "";
				u.whenCreated = availableValues.contains("whenCreated") ? dateToStringFormat(String.valueOf(metadata.get("whenCreated").get())) : "";
				u.pwdLastSet = availableValues.contains("pwdLastSet") ? toTimestamp(String.valueOf(metadata.get("pwdLastSet").get())) : "";
				u.logonCount = availableValues.contains("logonCount") ? String.valueOf(metadata.get("logonCount").get()) : "";
				u.isCriticalSystemObject = availableValues.contains("isCriticalSystemObject") ? String.valueOf(metadata.get("isCriticalSystemObject").get()) : "";
				u.description = availableValues.contains("description") ? deAccent(String.valueOf(metadata.get("description").get())) : "";
				u.accountExpires = availableValues.contains("pwdLastSet") ? convertExpiredDate(String.valueOf(metadata.get("pwdLastSet").get())) : ""; 
				u.lastLogoff = availableValues.contains("lastLogoff") ? String.valueOf(metadata.get("lastLogoff").get()) : "";
				u.lastLogonTimestamp = availableValues.contains("lastLogonTimestamp") ? toTimestamp(String.valueOf(metadata.get("lastLogonTimestamp").get())) : "";
				u.objectGUID = availableValues.contains("objectGUID") ? String.valueOf(metadata.get("objectGUID").get()) : "";
				u.lastLogon = availableValues.contains("lastLogon") ? toTimestamp(String.valueOf(metadata.get("lastLogon").get())) : "";
				u.uSNChanged = availableValues.contains("uSNChanged") ? String.valueOf(metadata.get("uSNChanged").get()) : "";
				u.uSNCreated = availableValues.contains("uSNCreated") ? String.valueOf(metadata.get("uSNCreated").get()) : "";
				u.countryCode = availableValues.contains("countryCode") ? String.valueOf(metadata.get("countryCode").get()) : "";
				u.instanceType = availableValues.contains("instanceType") ? String.valueOf(metadata.get("instanceType").get()) : "";
				u.memberOf = availableValues.contains("memberOf") ? String.valueOf(metadata.get("memberOf").get()) : "";
				u.badPwdCount = availableValues.contains("badPwdCount") ? String.valueOf(metadata.get("badPwdCount").get()) : "";
				u.objectSid = availableValues.contains("objectSid") ? String.valueOf(metadata.get("objectSid").get()) : "";

				users.add(u);
			}
			
			Control[] controls = ctx.getResponseControls();
            if (controls != null) {
                for (int i = 0; i < controls.length; i++) {
                    if (controls[i] instanceof PagedResultsResponseControl) {
                        PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                        cookie = prrc.getCookie();
                    }
                }
            }
            ctx.setRequestControls(new Control[]{new PagedResultsControl(pageSize, cookie, Control.CRITICAL)});
			}while (cookie != null);
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
		
		return users;
	}
	
	private Hashtable<String, String> getConnectionSettings(String username, String password) {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + this._host + ":" + this._port );
		env.put(Context.REFERRAL, "throw");
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.SECURITY_PRINCIPAL, username);
		env.put("java.naming.ldap.attributes.binary", "tokenGroups");
		return env;
	}
	
	private static String binarySidToStringSid(byte[] SID) {
		String strSID = "";
		// convert the SID into string format
		long version;
		long authority;
		long count;
		long rid;
		strSID = "S";
		version = SID[0];
		strSID = strSID + "-" + Long.toString(version);
		authority = SID[4];
		for (int i = 0; i < 4; i++) {
			authority <<= 8;
			authority += SID[4 + i] & 0xFF;
		}
		strSID = strSID + "-" + Long.toString(authority);
		count = SID[2];
		count <<= 8;
		count += SID[1] & 0xFF;
		for (int j = 0; j < count; j++) {
			rid = SID[11 + (j * 4)] & 0xFF;
			for (int k = 1; k < 4; k++) {
				rid <<= 8;
				rid += SID[11 - k + (j * 4)] & 0xFF;
			}
			strSID = strSID + "-" + Long.toString(rid);
		}
		return strSID;
	}
	
	static Long millisecondCorrector = 10000L;
    static Long epochCorrector = 11644473600000L;
    
	private static String toTimestamp(String input){
		Date data = new Date(Long.valueOf(input) / millisecondCorrector - epochCorrector);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(data);
		
	}
	
	private static String convertExpiredDate(String pwdLastSet){
		Date data = new Date((Long.valueOf(pwdLastSet) + Math.abs(-78624000000000L)) / millisecondCorrector - epochCorrector);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(data);
	}
	
	private static String dateToStringFormat(String inputDateTime) throws ParseException{
		String[] parts = inputDateTime.split("[.]");
		String dateTimePart = parts[0];
		String timeZonePart = "+0" + parts[1].substring(0, parts[1].length() - 1) + "00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
		Date date = sdf.parse(dateTimePart + timeZonePart);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(date);
	}
	
	private static String consolidatePhone(String phone){
		if(StringUtils.isNullOrEmpty(phone)) return StringUtils.Empty;
		return phone.trim().replace(" ", "").replace(".", "");
	}
	
	private  String deAccent(String str) {
		return str;
//	    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
//	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//	    return pattern.matcher(nfdNormalizedString).replaceAll("");
	}
	
	public static void main(String[] args) throws Exception {
        //LDAP://172.17.103.200:389/DC=mcredit,DC=local
		LDAPService service = new LDAPService();
		service.connect("dongtd.ho@mcredit.local", "Caikeo123");
		service.getUser("");
    }
}