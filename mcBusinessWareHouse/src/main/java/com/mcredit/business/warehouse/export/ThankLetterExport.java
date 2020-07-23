package com.mcredit.business.warehouse.export;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import report.docfile.FileCache;

import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.data.warehouse.entity.WhExpectedDate;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.warehouse.WareHouseThankLetter;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.util.StringUtils;
import com.mcredit.util.WordHandling;
import com.mcredit.util.ZipFiles;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ThankLetterExport implements IExportDocument {

    private UnitOfWorkWareHouse _uok = null;
    private CodeTableCacheManager ctCache = CacheManager.CodeTable();

    public ThankLetterExport(UnitOfWorkWareHouse uok) {
        _uok = uok;
    }

    @Override
    public String exportThankLetter(String contractNumber, int typeExport, String loginId) {
        List<WareHouseThankLetter> lst = this._uok.whDocumentRepo().getLstThankLetter(contractNumber, typeExport);
        FileCache docFile;
        ZipFiles zipFiles = new ZipFiles();
        Vector<String> vFiles = new Vector<String>();
        String urlDocFile = null;
        String pathTemplate = null;
        switch (typeExport) {
            case 1:
                pathTemplate = CacheManager.Parameters().findParamValueAsString(ParametersName.WH_THANKS_LETTER_TEMPLATE);
                break;
            case 2:
                pathTemplate = CacheManager.Parameters().findParamValueAsString(ParametersName.WH_THANKS_LETTER_TEMPLATE_F1);
                break;
            case 3:
                pathTemplate = CacheManager.Parameters().findParamValueAsString(ParametersName.WH_THANKS_LETTER_TEMPLATE_F2);
                break;
            default:
                break;
        }
        String url = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        zipFiles.deleteFiles(
                url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_THANKS_LETTER) + "/");

        long date = new Date().getTime();
        SimpleDateFormat formatNowDay = new SimpleDateFormat("dd");
        SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");

        String day = formatNowDay.format(date);
        String month = formatNowMonth.format(date);
        String year = formatNowYear.format(date);

        try {
            for (int i = 0; i < lst.size(); i++) {
                docFile = new FileCache();
                docFile.readFile(url + pathTemplate);

                docFile.setParam("[CONTRACT_NUMBER]", StringUtils.consolidate(lst.get(i).getContractNumber()));
                docFile.setParam("[CUST_NAME]", StringUtils.consolidate(lst.get(i).getCustName()));
                docFile.setParam("[IDENTITY_NUMBER]", StringUtils.consolidate(lst.get(i).getIdentityNumber()));
                docFile.setParam("[IDENTITY_ISSUE_DATE]", StringUtils.consolidate(lst.get(i).getIdentityIssueDate()));
                docFile.setParam("[IDENTITY_ISSUE_PLACE]", StringUtils.consolidate(lst.get(i).getIdentityIssuePlace()));
                docFile.setParam("[ADDRESS]", StringUtils.consolidate(lst.get(i).getAddress()));
                docFile.setParam("[PHONE_NUMBER]", StringUtils.consolidate(lst.get(i).getContactValue()));
                docFile.setParam("[START_DATE]", StringUtils.consolidate(lst.get(i).getActualOpenedDate()));
                docFile.setParam("[END_DATE]", StringUtils.consolidate(lst.get(i).getActualClosedDate()));
                if (typeExport == 2 || typeExport == 3) {
                    if (lst.get(i).getNplate() != null && !lst.get(i).getNplate().equals("")) {
                        docFile.setParam("[N_PLATE]", StringUtils.consolidate(lst.get(i).getNplate()));
                    } else {
                        docFile.setParam("[N_PLATE]", "");
                    }                    
                }

                docFile.setParam("[dd]", day);
                docFile.setParam("[mm]", month);
                docFile.setParam("[yyyy]", year);

                //Log export thank letter
                Long changeStatus = ctCache.getIdBy(CTCodeValue1.WH_SEND_THANKLETTER, CTCat.WH_HISTORY_EXPORT);
                WhDocumentChange item = new WhDocumentChange(Long.parseLong(lst.get(i).getCreditAppId()), changeStatus,
                        new Date(), loginId);
                this._uok.whDocumentChangeRepo().add(item);
                
                this._uok.whExpectedDateRepo().deleteExpectedDate(Long.parseLong(lst.get(i).getCreditAppId()), changeStatus);
                WhExpectedDate whExpectedDate = new WhExpectedDate(Long.parseLong(lst.get(i).getCreditAppId()), changeStatus, new Date());
                this._uok.whExpectedDateRepo().upsert(whExpectedDate);

                //Write file
                String strFileFullPath = CacheManager.Parameters().findParamValueAsString(
                        ParametersName.WH_THANKS_LETTER_FILE_NAME) + lst.get(i).getContractNumber() + ".doc";
                String outputPath = url + strFileFullPath;
                docFile.writeFile(outputPath);

                System.out.println(outputPath.substring(1, outputPath.length()));
                //Merge file
                vFiles.add(outputPath);

            }
            PrintWriter os = new PrintWriter(new File(url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_EXPORT_THANK_LETTER)));

            // Writes the first line of the first file
            BufferedReader file = new BufferedReader(new FileReader(vFiles.get(0)));
            String firstLine = file.readLine();

            os.println(firstLine);
            file.close();

            String lastLine = WordHandling.concatenate2(os, vFiles);
            // Writes the last line of the last file
            os.println(lastLine);

            os.close();

//            File dir = new File(
//                    url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_THANKS_LETTER));
            String urlDocDirName = url
                    + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_EXPORT_THANK_LETTER);

//            zipFiles.zipDirectory(dir, zipDirName);
            urlDocFile = urlDocDirName.substring(1, urlDocDirName.length());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return urlDocFile;
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public String export(String contractNumber, String loginId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
