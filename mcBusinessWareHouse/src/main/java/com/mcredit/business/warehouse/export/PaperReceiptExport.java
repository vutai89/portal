package com.mcredit.business.warehouse.export;

import java.io.File;
import java.io.IOException;
import java.util.List;

import report.docfile.FileCache;

import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.warehouse.WareHousePaperReceipt;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.util.StringUtils;
import com.mcredit.util.WordHandling;
import com.mcredit.util.ZipFiles;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class PaperReceiptExport implements IExportDocument {

    private UnitOfWorkWareHouse _uok = null;
    private CodeTableCacheManager ctCache = CacheManager.CodeTable();

    public PaperReceiptExport(UnitOfWorkWareHouse uok) {
        _uok = uok;
    }

    @Override
    public String export(String contractNumber, String loginId) {
        List<WareHousePaperReceipt> items = this._uok.whDocumentRepo().getLstPaperReceipt(contractNumber);
        ZipFiles zipFiles = new ZipFiles();
        Vector<String> vFiles = new Vector<String>();
        String urlDocFile = null;
        FileCache docFile;
        String pathTemplate = CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PAPER_RECEIPT_TEMPLATE);
        String url = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        zipFiles.deleteFiles(
                url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_PAPER_RECEIPT) + "/");
        
        long date = new Date().getTime();
        SimpleDateFormat formatNowDay = new SimpleDateFormat("dd");
        SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");

        String day = formatNowDay.format(date);
        String month = formatNowMonth.format(date);
        String year = formatNowYear.format(date);

        try {
            for (int i = 0; i < items.size(); i++) {
                docFile = new FileCache();
                docFile.readFile(url + pathTemplate);

                docFile.setParam("[COUNT]", StringUtils.consolidate(this._uok.whDocumentChangeRepo().countExportPaperReceipt(Long.parseLong(items.get(i).getCreditAppId()))));
                docFile.setParam("[CONTRACT_NUMBER]", StringUtils.consolidate(items.get(i).getContractNumber()));
                docFile.setParam("[BPM_APP_NUMBER]", StringUtils.consolidate(items.get(i).getBpmAppNumber()));
                docFile.setParam("[CUST_NAME]", StringUtils.consolidate(items.get(i).getCustName()));
                docFile.setParam("[IDENTITY_NUMBER]", StringUtils.consolidate(items.get(i).getIdentityNumber()));
                docFile.setParam("[IDENTITY_ISSUE_DATE]", StringUtils.consolidate(items.get(i).getIdentityIssueDate()));
                docFile.setParam("[IDENTITY_ISSUE_PLACE]", StringUtils.consolidate(items.get(i).getIdentityIssuePlace()));
                docFile.setParam("[ADDRESS]", StringUtils.consolidate(items.get(i).getAddress()));
                docFile.setParam("[CAVET_NUMBER]", StringUtils.consolidate(items.get(i).getCavetNumber()));

                if (items.get(i).getCavetNumber() != null && !items.get(i).getCavetNumber().equals("")) {
                    docFile.setParam("[CAVET_NUMBER]", items.get(i).getCavetNumber());
                } else if (items.get(i).getCavetNumberBPM() != null && !items.get(i).getCavetNumberBPM().equals("")) {
                    docFile.setParam("[CAVET_NUMBER]", items.get(i).getCavetNumberBPM());
                } else {
                    docFile.setParam("[CAVET_NUMBER]", "");
                }

                if (items.get(i).getBrand() != null && !items.get(i).getBrand().equals("")) {
                    docFile.setParam("[BRAND]", items.get(i).getBrand());
                } else if (items.get(i).getBrandBPM() != null && !items.get(i).getBrandBPM().equals("")) {
                    docFile.setParam("[BRAND]", items.get(i).getBrandBPM());
                } else {
                    docFile.setParam("[BRAND]", "");
                }
                if (items.get(i).getModelCode() != null && !items.get(i).getModelCode().equals("")) {
                    docFile.setParam("[MODEL_CODE]", items.get(i).getModelCode());
                } else if (items.get(i).getModelCodeBPM() != null && !items.get(i).getModelCodeBPM().equals("")) {
                    docFile.setParam("[MODEL_CODE]", items.get(i).getModelCodeBPM());
                } else {
                    docFile.setParam("[MODEL_CODE]", "");
                }
                if (items.get(i).getChassis() != null && !items.get(i).getChassis().equals("")) {
                    docFile.setParam("[CHASSIS]", items.get(i).getChassis());
                } else if (items.get(i).getChassisBPM() != null && !items.get(i).getChassisBPM().equals("")) {
                    docFile.setParam("[CHASSIS]", items.get(i).getChassisBPM());
                } else {
                    docFile.setParam("[CHASSIS]", "");
                }
                if (items.get(i).getEngine() != null && !items.get(i).getEngine().equals("")) {
                    docFile.setParam("[ENGINE]", items.get(i).getEngine());
                } else if (items.get(i).getEngineBPM() != null && !items.get(i).getEngineBPM().equals("")) {
                    docFile.setParam("[ENGINE]", items.get(i).getEngineBPM());
                } else {
                    docFile.setParam("[ENGINE]", "");
                }
                if (items.get(i).getnPlate() != null && !items.get(i).getnPlate().equals("")) {
                    docFile.setParam("[N_PLATE]", items.get(i).getnPlate());
                } else if (items.get(i).getnPlateBPM() != null && !items.get(i).getnPlateBPM().equals("")) {
                    docFile.setParam("[N_PLATE]", items.get(i).getnPlateBPM());
                } else {
                    docFile.setParam("[N_PLATE]", "");
                }

                docFile.setParam("[DATE_GBN]", day + "/" + month + "/" + year);
                if (items.get(i).getActualClosedDate() != null) {
                    docFile.setParam("[CLOSED_DATE]", items.get(i).getActualClosedDate());
                } else {
                    docFile.setParam("[CLOSED_DATE]", "");
                }

                docFile.setParam("[dd]", day);
                docFile.setParam("[mm]", month);
                docFile.setParam("[yyyy]", year);

                //save history
                Long typeExport = ctCache.getIdBy(CTCodeValue1.WH_EXPORT_PAPER_RECEIPT, CTCat.WH_HISTORY_EXPORT);
                WhDocumentChange item = new WhDocumentChange(Long.parseLong(items.get(i).getCreditAppId()), typeExport,
                        new Date(), loginId);
                this._uok.whDocumentChangeRepo().add(item);

                //Write file
                String strFileFullPath = CacheManager.Parameters().findParamValueAsString(
                        ParametersName.WH_PAPER_RECEIPT_FILE_NAME) + items.get(i).getContractNumber() + ".doc";
                String outputPath = url + strFileFullPath;
                docFile.writeFile(outputPath);

                System.out.println(outputPath.substring(1, outputPath.length()));
                //Merge file
                vFiles.add(outputPath);
            }

            PrintWriter os = new PrintWriter(new File(url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_EXPORT_PAPER_RECEIPT)));

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
//                    url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_PAPER_RECEIPT));
            String urlDocDirName = url
                    + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_EXPORT_PAPER_RECEIPT);

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
    public String exportThankLetter(String contractNumber, int typeExport, String loginId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
