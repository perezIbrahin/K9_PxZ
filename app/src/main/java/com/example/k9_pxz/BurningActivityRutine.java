package com.example.k9_pxz;

import static com.itextpdf.text.pdf.XfaXpathConstructor.XdpPackage.Pdf;

import static Permission.PermissionsActivity.PERMISSION_REQUEST_CODE;
import static Permission.PermissionsChecker.REQUIRED_PERMISSION;
import static Util.LogUtils.LOGE;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CancellationSignal;

import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//


//
/*
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;*/


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.BasePermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


import Adapter.RecyclerViewAdapBurn;
import Alert.CustomAlert;
import Interface.RecyclerViewClickInterface;
import Model.ModelBurn;
import Permission.PermissionsActivity;
import Permission.PermissionsChecker;
import Util.Common;
import Util.FileUtils;
import Util.PdfDocumentAdapter;
import Util.Util_Burn;

public class BurningActivityRutine extends AppCompatActivity implements RecyclerViewClickInterface, View.OnClickListener {
    private static final String TAG = "BurningActivityRutine";
    private static final int MY_PERMISSIONS_REQUEST =1;

    /*
     *
     * https://www.youtube.com/watch?v=gpH4Zr1ffnU*/
    //GUI
    private Button btnBurnStart;
    private Button btnBurnStop;
    private Button btnBurnPause;
    private Button btnBurnReport;
    private Button btnHome;

    //private String
    private String BLE_ADD_GOT = "0";
    private String SERIAL_NUMBER = "0";
    CustomAlert customAlert;

    public String DATA_BLE_ADD = "DATA_BLE_ADD";
    public String DATA_SYSTEM_SERIAL = "DATA_SYSTEM_SERIAL";


    private Util_Burn utilBurn = new Util_Burn();

    //RecyclerView
    private RecyclerView recyclerViewBurn;
    //Adapter
    private RecyclerViewAdapBurn viewAdapBurn = new RecyclerViewAdapBurn();
    //arraylist-Recycler View Adapter
    private ArrayList<ModelBurn> modelBurns = new ArrayList<>();

    Context mContext;

    PermissionsChecker checker;

    String dest;

    String mPath = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burning_rutine);

        firstTimeLoadGUI();
        initGUI();
        eventsBtn();

        checkPermissions();

        createPDF();

        //checker = new PermissionsChecker(this);



        /*mContext = getApplicationContext();

        dest = FileUtils.getAppPath(mContext) + "123.pdf";*/
    }

    //inject data to the model scan
    private ModelBurn injectDataModelBurn(String modName, String modFreq, String modInt, String modTime, String modCycles, String modStatus) {
        ModelBurn modelBurn = new ModelBurn();
        modelBurn.setModName(modName);
        modelBurn.setModFreq(modFreq);
        modelBurn.setModInt(modInt);
        modelBurn.setModTime(modTime);
        modelBurn.setModCycles(modCycles);
        modelBurn.setModStatus(modStatus);
        //Log.d(TAG, "injectDataModelScan:name: " + modelScan.getDeviceName() + ".UUID:" + modelScan.getDevUIID());
        return modelBurn;
    }

    //update Recycler view
    private void updateRecyclerViewBurn() {
        recyclerViewBurn = findViewById(R.id.recyclerViewBurn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewBurn.setLayoutManager(linearLayoutManager);
        recyclerViewBurn.setHasFixedSize(true);
        viewAdapBurn = new RecyclerViewAdapBurn(this, modelBurns);//modelDevicesArrayList
        recyclerViewBurn.setAdapter(viewAdapBurn);
    }

    //update GUI adapter Burn
    private boolean updateGuiRecyclerViewBurn(String modName, String modFreq, String modInt, String modTime, String modCycles, String modStatus) {
        if (modelBurns != null) {
            //modelScan.clear();
            Log.d(TAG, "updateGuiRecyclerViewMainDev: ");
            //modelScan.clear();
            modelBurns.add(injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
            return true;
        }
        return false;
    }

    //clean model
    private void cleanModel() {
        modelBurns.clear();
    }

    //update position
    private void updateBurnGUI(int module, String modName, String modFreq, String modInt, String modTime, String modCycles, String modStatus) {
        if (modelBurns == null) {
            return;
        }

        switch (module) {
            case 1:
                modelBurns.add(0, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 2:
                modelBurns.add(1, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 3:
                modelBurns.add(2, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 4:
                modelBurns.add(3, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 5:
                modelBurns.add(4, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
        }

    }

    //loading first time
    private void firstTimeLoadGUI() {
        for (int i = 1; i < utilBurn.MAX_NUMBER_TRANS + 1; i++) {
            Log.d(TAG, "firstTimeLoadGUI:i: " + i);
            String modName = utilBurn.MOD_DEF_NAME + String.valueOf(i);
            updateBurnGUI(i, modName, utilBurn.MOD_DEF_FREQ, utilBurn.MOD_DEF_INT, utilBurn.MOD_DEF_TIME, utilBurn.MOD_DEF_CYCLES, utilBurn.MOD_DEF_STATUS);
        }
        updateRecyclerViewBurn();
    }

    //init GUI
    private void initGUI() {
        btnBurnStart = findViewById(R.id.btnBurnStart);
        btnBurnStop = findViewById(R.id.btnBurnStop);
        btnBurnPause = findViewById(R.id.btnBurnPause);
        btnBurnReport = findViewById(R.id.btnBurnReport);
        btnHome = findViewById(R.id.btnHome);
    }

    //events buttons
    private void eventsBtn() {
        btnBurnStart.setOnClickListener(this);
        btnBurnStop.setOnClickListener(this);
        btnBurnPause.setOnClickListener(this);
        btnBurnReport.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onItemPostSelect(int position, String value) {

    }

    @Override
    public void onClick(View v) {
        if (v == btnBurnStart) {

        } else if (v == btnBurnStop) {

        } else if (v == btnBurnPause) {

        } else if (v == btnBurnReport) {
            //createPDF();
        } else if (v == btnHome) {
            goHome();
        }
    }

    //////////////************************//
   /* public void createPdf(String dest) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {



             //Creating Document

            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(dest));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            PdfDocumentInfo info = pdfDocument.getDocumentInfo();

            info.setTitle("Example of iText7 by Pratik Butani");
            info.setAuthor("Pratik Butani");
            info.setSubject("iText7 PDF Demo");
            info.setKeywords("iText, PDF, Pratik Butani");
            info.setCreator("A simple tutorial example");

            Document document = new Document(pdfDocument, PageSize.A4, true);


            //Variables for further use....

            Color mColorAccent = new DeviceRgb(153, 204, 255);
            Color mColorBlack = new DeviceRgb(0, 0, 0);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;


             //How to USE FONt

            PdfFont font = PdfFontFactory.createFont("assets/fonts/brandon_medium.otf", "UTF-8", true);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator(new DottedLine());
            lineSeparator.setStrokeColor(new DeviceRgb(0, 0, 68));

            // Title Order Details...
            // Adding Title....
            Text mOrderDetailsTitleChunk = new Text("Order Details").setFont(font).setFontSize(36.0f).setFontColor(mColorBlack);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(mOrderDetailsTitleParagraph);

            // Fields of Order Details...
            // Adding Chunks for Title and value
            Text mOrderIdChunk = new Text("Order No:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            Text mOrderIdValueChunk = new Text("#123123").setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderIdValueParagraph = new Paragraph(mOrderIdValueChunk);
            document.add(mOrderIdValueParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(lineSeparator);
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Text mOrderDateChunk = new Text("Order Date:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderDateParagraph = new Paragraph(mOrderDateChunk);
            document.add(mOrderDateParagraph);

            Text mOrderDateValueChunk = new Text("06/07/2017").setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            document.add(mOrderDateValueParagraph);

            document.add(new Paragraph(""));
            document.add(lineSeparator);
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Text mOrderAcNameChunk = new Text("Account Name:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            document.add(mOrderAcNameParagraph);

            Text mOrderAcNameValueChunk = new Text("Pratik Butani").setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderAcNameValueParagraph = new Paragraph(mOrderAcNameValueChunk);
            document.add(mOrderAcNameValueParagraph);

            document.add(new Paragraph(""));
            document.add(lineSeparator);
            document.add(new Paragraph(""));

            document.close();

            Toast.makeText(mContext, "Created... :)", Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            LOGE("createPdf: Error " + e.getLocalizedMessage());
        } catch (ActivityNotFoundException | DocumentException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            Toast.makeText(mContext, "Permission Granted to Save", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Permission not granted, Try again!", Toast.LENGTH_SHORT).show();
        }
    }
    public void createPDF(View view) {
        if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
            PermissionsActivity.startActivityForResult(BurningActivityRutine.this, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
        } else {
            createPdf(dest);
        }
    }

    public void openPDF(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtils.openFile(mContext, new File(dest));
                } catch (Exception e) {
                    Log.d("TAG", "run: ERror");
                }
            }
        }, 1000);
    }*/


    //////////////************************//

    //old
    //create pdf

    private void createPDF() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BasePermissionListener() {
                                  @Override
                                  public void onPermissionGranted(PermissionGrantedResponse response) {
                                      super.onPermissionGranted(response);
                                      Log.d(TAG, "onPermissionGranted: ");
                                      btnBurnReport.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Log.d(TAG, "onClick: btnBurnReport");
                                              try {
                                                  createPDFFile(Common.getAppPath(BurningActivityRutine.this) + "test_pdf.pdf");
                                              } catch (Exception e) {
                                                  e.printStackTrace();
                                              }
                                          }
                                      });

                                  }

                                  @Override
                                  public void onPermissionDenied(PermissionDeniedResponse response) {

                                      super.onPermissionDenied(response);
                                      Log.d(TAG, "onPermissionDenied: ");
                                  }

                                  @Override
                                  public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                      super.onPermissionRationaleShouldBeShown(permission, token);
                                      Log.d(TAG, "onPermissionRationaleShouldBeShown: ");
                                  }
                              }
                ).check();
    }

    private void createPDFFile(String path) throws Exception {
        if (new File(path).exists())
            new File(path).delete();
        Log.d(TAG, "createPDFFile: file exist.Will be deleted");
        mPath = path;
        try {
            Document document = new Document();
            //save
            Log.d(TAG, "createPDFFile: path:" + path);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open to write
            document.open();
            Log.d(TAG, "createPDFFile: document is open:"+document.isOpen());
            //settings
            document.setPageSize(PageSize.LETTER);
            document.addCreationDate();
            document.addAuthor("kap Medical");
            document.addCreator("Report K9");
            //from settings
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSize = 20.0f;
            float valueFontSize = 10.0f;//26.0f
            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            //create title of document
            Font titleFont = new Font(fontName, 18.0f, Font.NORMAL, BaseColor.BLACK);//36.0f
            Font subTitleFont = new Font(fontName, 12.0f, Font.NORMAL, BaseColor.BLUE);//36.0f
            addNewItem(document, "Kap Medical", Element.ALIGN_CENTER, titleFont);

            //addNewItem(document, "Report burning-K9", Element.ALIGN_CENTER, titleFont);
            //Add more
            Font orderNumberFont = new Font(fontName, valueFontSize, Font.NORMAL, colorAccent);
            addNewItem(document, "Report No", Element.ALIGN_CENTER, orderNumberFont);

            Font orderNumberValueFont = new Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK);
            String uuid=createTransactionID();
            addNewItem(document, "#717121", Element.ALIGN_CENTER, orderNumberValueFont);
            //addNewItem(document, "#"+uuid, Element.ALIGN_CENTER, orderNumberValueFont);

            //separator
            addLineSeparator(document);

            addNewItem(document, "Report Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "03/11/22", Element.ALIGN_LEFT, orderNumberFont);
            //separator
            addLineSeparator(document);

            addNewItem(document, "Report Name", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "Burning K9", Element.ALIGN_LEFT, orderNumberFont);

            //separator
            addLineSeparator(document);
            addNewItem(document, "Burning Process Details", Element.ALIGN_CENTER, titleFont);
            addLineSeparator(document);

            //item1
            addNewItemWithLeftAndright(document, "K9-Module 1", "(100.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Parameters:", "F:30Hz,T:25min,I:80%", subTitleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Error:", "0", subTitleFont, orderNumberValueFont);
            addLineSeparator(document);
            //item2
            addNewItemWithLeftAndright(document, "K9-Module 2", "(50.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Parameters:", "F:30Hz,T:25min,I:80%", subTitleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Error:", "1", subTitleFont, orderNumberValueFont);
            addLineSeparator(document);
            //item3
            addNewItemWithLeftAndright(document, "K9-Module 3", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Parameters:", "F:30Hz,T:25min,I:80%", subTitleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Error:", "0", subTitleFont, orderNumberValueFont);
            addLineSeparator(document);
            //item4
            addNewItemWithLeftAndright(document, "K9-Module 4", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Parameters:", "F:30Hz,T:25min,I:80%", subTitleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Error:", "0", subTitleFont, orderNumberValueFont);
            addLineSeparator(document);
            //item5
            addNewItemWithLeftAndright(document, "K9-Module 5", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Parameters:", "F:30Hz,T:25min,I:80%", subTitleFont, orderNumberValueFont);
            addNewItemWithLeftAndright(document, "  Error:", "0", subTitleFont, orderNumberValueFont);
            addLineSeparator(document);
            //total
            addLineSpace(document);
            addNewItem(document, "Notes:", Element.ALIGN_LEFT, subTitleFont);
            addNewItem(document, "  ->", Element.ALIGN_LEFT, subTitleFont);
            addNewItem(document, "  ->", Element.ALIGN_LEFT, subTitleFont);
            addNewItem(document, "  ->", Element.ALIGN_LEFT, subTitleFont);
            addLineSpace(document);
            addLineSeparator(document);
            addNewItemWithLeftAndright(document, "Report status:", "(Burning fail)", titleFont, titleFont);
            addLineSpace(document);


            //
            Log.d(TAG, "createPDFFile: document created");
            document.close();

            Log.d(TAG, "createPDFFile: document closed");

            //Toast.makeText(getApplicationContext(), "Sucess!!!", Toast.LENGTH_SHORT).show();

            printPDF();

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {

            String pathLocal = "/storage/emulated/0/K9_PxZ/test_pdf.pdf";
            //Log.d(TAG, "printPDF: get path:" + Common.getAppPath(BurningActivityRutine.this) + mPath);
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(BurningActivityRutine.this, Common.getAppPath(BurningActivityRutine.this) + mPath);
            //PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(BurningActivityRutine.this, path);//path//"test_pdf.pdf"
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        } catch (Exception e) {
            Log.d(TAG, "printPDF: Ex" + e.getMessage());

        }

    }

    private void addNewItemWithLeftAndright(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkextRight);
        document.add(p);
    }

    //line separator
    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    //line space
    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }


    //go to home
    private void goHome() {
        Bundle bundle = new Bundle();
        Log.d(TAG, "onClick: get address " + BLE_ADD_GOT);
        bundle.putString(DATA_BLE_ADD, BLE_ADD_GOT);//
        bundle.putString(DATA_SYSTEM_SERIAL, SERIAL_NUMBER);
        Intent intent = new Intent(BurningActivityRutine.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //check permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               // saveToExcel.writeToExcel("machines","names","reasons","treatments","times");

                //saveToExcel.writeToExcel(name,sex,phone,address);
                Log.d(TAG, "onRequestPermissionsResult: granted");
                Toast.makeText(BurningActivityRutine.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            } else {

                // Permission Denied
                Log.d(TAG, "onRequestPermissionsResult: denied");
                Toast.makeText(BurningActivityRutine.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    //write to exxcel
    public static String getExcelDir() {

        // SD
        String sdcardPath = Environment.getExternalStorageDirectory()
                .toString();
        File dir = new File(sdcardPath + File.separator + "Excel"
                + File.separator + "Person");
        //File dir = new File(sdcardPath + File.separator + "Excel");
        if (!dir.exists()) {

            return dir.toString();
        } else {

            dir.mkdirs();
            Log.e("BAG", "保存路径不存在,");
            return dir.toString();
        }
    }

    //get uuid
    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}