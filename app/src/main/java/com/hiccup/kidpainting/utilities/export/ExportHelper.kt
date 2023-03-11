package com.hiccup.kidpainting.utilities.export

class ExportHelper {

//    fun sendExportToEmail(File myFile) throws IOException
//    {
//        Uri path = Uri . fromFile (myFile);
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        // set the type to 'email'
//        emailIntent.setType("vnd.android.cursor.dir/email");
//        String to [] = { "binhpd.55cb@gmail.com" };
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
//        // the attachment
//        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
//        // the mail subject
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//        startActivity(Intent.createChooser(emailIntent, "Send email..."));
//    }
//
//    fun saveToStorage()
//    {
//        File exportFile = null;
//        try {
//            Toast.makeText(getBaseContext(), "Exported",
//                    Toast.LENGTH_SHORT).show();
//            exportFile = writeStringToFile();
//        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), e.getMessage(),
//                    Toast.LENGTH_SHORT).show();
//        }
//
//
//        try {
//            sendExportToEmail(exportFile);
//        } catch (IOException e) {
//            Toast.makeText(getBaseContext(), e.getMessage(),
//                    Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }
//
//    fun writeStringToFile() : File
//    {
//        val displaymetrics = DisplayMetrics()
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics)
//        int height = displaymetrics . heightPixels;
//        int mWidth = displaymetrics . widthPixels;
//
//        //TODO: save points to file
//                return StateDrawingWriter.writeStringToFile(mDrawView.getGetListPath(), mWidth, height);
//    }
}