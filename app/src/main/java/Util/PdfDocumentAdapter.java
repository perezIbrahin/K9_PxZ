package Util;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import com.example.k9_pxz.BurningActivityRutine;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends PrintDocumentAdapter {
    private static final String TAG = "PdfDocumentAdapter";
    Context context;
    String path;

    public PdfDocumentAdapter(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    /*public PdfDocumentAdapter(BurningActivityRutine burningActivityRutine, String s) {
    }*/

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

        if(cancellationSignal.isCanceled()){
            callback.onLayoutCancelled();
        }else{
            PrintDocumentInfo.Builder builder=new PrintDocumentInfo.Builder("file name");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();
            callback.onLayoutFinished(builder.build(),!printAttributes1.equals(printAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream in = null;
        OutputStream out=null;
        try {
            Log.d(TAG, "onWrite: path"+path);
            String pathLocal = "/storage/emulated/0/K9_PxZ/test_pdf.pdf";
            File file = new File(pathLocal);
            //File file = new File(path);
            in = new FileInputStream(file);
            out = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            byte[] buff = new byte[16384];
            int size;
            while ((size = in.read(buff)) >= 0 && !cancellationSignal.isCanceled()) {
                out.write(buff, 0, size);
            }
            if (cancellationSignal.isCanceled()) {
                writeResultCallback.onWriteCancelled();
            } else {
                writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }


        }catch (Exception e){
            writeResultCallback.onWriteFailed(e.getMessage());
            Log.d(TAG, "onWrite: exception:"+e.getMessage());
            e.printStackTrace();
        }
        finally {
          /*  try {
                //in.close();
               // out.close();
            } catch (IOException e) {
                Log.d(TAG, "onWrite: Exce"+e.getMessage());
                e.printStackTrace();
            }*/


        }


    }
}
