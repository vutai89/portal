package com.mcredit.rule;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RuleCacheManager {

	private Map<String, KieBase> kieBaseCache;
	//private static final Logger logger = LogManager.getLogger(RuleCacheManager.class);
	private String baseFolder= "src/main/resources/";
	private static RuleCacheManager instance;

	public static synchronized RuleCacheManager getInstance() {
		if (instance == null) {
			synchronized (RuleCacheManager.class) {
				if (null == instance) {
					instance = new RuleCacheManager();
				}
			}
		}
		return instance;
	}

	public RuleCacheManager() {
		initRuleBase();
	}
	
	private void initRuleBase() {
		if(kieBaseCache == null) {
			kieBaseCache = new HashMap<String, KieBase>();
			Properties props = new Properties();
			String[] filelist;
			try {
				props.load(RuleCacheManager.class.getResourceAsStream("/rules.properties"));
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
			for(int i=1;i<10;i++) {
				if(props.getProperty("rule.list.file.name"+i) != null) {
					filelist = props.getProperty("rule.list.file.name"+i).split(";");
					if(filelist != null && filelist.length > 0) {
						for(String s : filelist) {
							buildRuleBase(s.trim());
						}
					}
				}
			}
		}
	}

	public synchronized KieSession getSession(String drlFile) {
		System.out.println("Get existing rules KieBase from Cache for " + drlFile);
		//initRuleBase();
		if(kieBaseCache == null) {
			kieBaseCache = new HashMap<String, KieBase>();
		}
		KieBase kb = kieBaseCache.get(drlFile);
		if(kb == null) {
			kb = buildRuleBase(drlFile);
		}
		return kb.newKieSession();
	}

	private KieBase buildRuleBase(String drlFile) {
		try {
			String drlPath = "rules/" + drlFile;
			KieServices ks = KieServices.Factory.get();
			KieFileSystem kfs = ks.newKieFileSystem();
			//Path path = Paths.get(getClass().getResource(drlPath).toURI());
			ClassLoader resourceLoader = Thread.currentThread().getContextClassLoader();
			InputStream stream = resourceLoader.getResourceAsStream(drlPath);
//			String content = new String(Files.readAllBytes(path),
//					Charset.forName("UTF-8"));
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[524288];

			while ((nRead = stream.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
			buffer.flush();
			String content = new String(buffer.toByteArray(), Charset.forName("UTF-8"));

			kfs.write(baseFolder + drlPath, ks.getResources()
							.newReaderResource(new StringReader(content))
							.setResourceType(ResourceType.DRL));

			KieBuilder kb = ks.newKieBuilder(kfs);

			kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
			if (kb.getResults().hasMessages(Message.Level.ERROR)) {
				System.out.println(kb.getResults().toString());
			    throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
			}

			KieContainer kContainer = ks.newKieContainer(kb.getKieModule().getReleaseId());
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			KieBase kbase = kContainer.newKieBase(kbconf);
			System.out.println("Put rules KieBase into Custom Cache - drlFile:= " + drlFile);
			kieBaseCache.put(drlFile, kbase);
			return kbase;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void refresh() {
		kieBaseCache = null;
		initRuleBase();
	}
}
