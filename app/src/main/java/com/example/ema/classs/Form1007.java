package com.example.ema.classs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.example.ema.R;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Form1007 implements Serializable {
    private String id;
    private String orderId;
    private String garageName;
    private String garageId;
    private String equipmentId;
    private String equipmentType;
    private String repairType;
    private String speedometer;
    private String fuelAmount;
    private String enterDate;
    private String details;
    private String firstCost;
    private String endCost;
    private String exitDate;
    private String unit;
    private String currentUserName;
    private String currentUserId;
    private String currentUserDarga;
    private String openDate;
    private String openUserEmail;
    private String openUserSignature;
    private String closeUserName;
    private String closeUserId;
    private String closeUserDarga;
    private String finishUserName;
    private String finishUserId;
    private String finishUserDarga;
    private String finishDate;
    private String finishUserSignature;
    private String increaseUserName;
    private String increaseUserId;
    private String increaseUserDarga;
    private String increaseDate;
    private String increaseUserSignature;
    private String billId;
    private String closeDate;
    private String closeUserSignature;
    private List<String> forms1007Signed;
    private List<String> form1028;
    private List<String> estimats;
    private List<String> bill;
    private String status;
    private String equipmentFamily;



    public Form1007(String equipmentFamily, User currentUser, Bitmap openUserSignature, String orderId, String garageName, String garageId, String equipmentId, String equipmentType, String repairType,
                    String speedometer, String fuelAmount, String enterDate, String details, String firstCost, String unit) {
        forms1007Signed=new ArrayList<String>();
        form1028=new ArrayList<String>();
        estimats=new ArrayList<String>();
        bill=new ArrayList<String>();

        this.equipmentFamily=equipmentFamily;
        this.openUserEmail=currentUser.getEmail();
        this.id = String.valueOf(this.hashCode());
        this.orderId = orderId;
        this.garageName = garageName;
        this.garageId = garageId;
        this.equipmentId = equipmentId;
        this.equipmentType = equipmentType;
        this.repairType = repairType;
        this.speedometer = speedometer;
        this.fuelAmount = fuelAmount;
        this.enterDate = enterDate;
        this.details = details;
        this.firstCost = firstCost;
        this.unit = unit;
        this.endCost = null;
        this.exitDate = null;
        this.currentUserName = currentUser.getName();
        this.currentUserId = currentUser.getId();
        this.currentUserDarga = currentUser.getDarga();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

        this.openDate = formattedDate;
        this.openUserSignature = AdapterBitmap.adaptToString(openUserSignature);

        this.increaseUserId=null;
        this.increaseUserDarga=null;
        this.increaseDate=null;
        this.increaseUserName=null;
        this.increaseUserSignature=null;

        this.closeUserId = null;
        this.closeUserDarga = null;
        this.closeUserName = null;
        this.billId = null;
        this.closeDate = null;
        this.closeUserSignature = null;
        this.status=null;

         this.finishUserName=null;
        this.finishUserId=null;
        this.finishUserDarga=null;
        this.finishDate=null;
        this.finishUserSignature=null;

        this.status="טרם אושר";




    }
    public Form1007(){};

    public String getIncreaseUserName() {
        return increaseUserName;
    }

    public void setIncreaseUserName(String increaseUserName) {
        this.increaseUserName = increaseUserName;
    }

    public String getIncreaseUserId() {
        return increaseUserId;
    }

    public void setIncreaseUserId(String increaseUserId) {
        this.increaseUserId = increaseUserId;
    }

    public String getIncreaseUserDarga() {
        return increaseUserDarga;
    }

    public void setIncreaseUserDarga(String increaseUserDarga) {
        this.increaseUserDarga = increaseUserDarga;
    }

    public String getIncreaseDate() {
        return increaseDate;
    }

    public void setIncreaseDate(String increaseDate) {
        this.increaseDate = increaseDate;
    }

    public String getIncreaseUserSignature() {
        return increaseUserSignature;
    }

    public void setIncreaseUserSignature(String increaseUserSignature) {
        this.increaseUserSignature = increaseUserSignature;
    }

    public String getEquipmentFamily() {
        return equipmentFamily;
    }

    public void setEquipmentFamily(String equipmentFamily) {
        this.equipmentFamily = equipmentFamily;
    }

    public String getOpenUserEmail() {
        return openUserEmail;
    }

    public void setOpenUserEmail(String openUserEmail) {
        this.openUserEmail = openUserEmail;
    }

    public String getCloseUserName() {
        return closeUserName;
    }

    public String getCloseUserId() {
        return closeUserId;
    }

    public String getCloseUserDarga() {
        return closeUserDarga;
    }

    public String getFinishUserName() {
        return finishUserName;
    }

    public void setFinishUserName(String finishUserName) {
        this.finishUserName = finishUserName;
    }

    public String getFinishUserId() {
        return finishUserId;
    }

    public void setFinishUserId(String finishUserId) {
        this.finishUserId = finishUserId;
    }

    public String getFinishUserDarga() {
        return finishUserDarga;
    }

    public void setFinishUserDarga(String finishUserDarga) {
        this.finishUserDarga = finishUserDarga;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getFinishUserSignature() {
        return finishUserSignature;
    }

    public void setFinishUserSignature(String finishUserSignature) {
        this.finishUserSignature = finishUserSignature;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getForms1007Signed() {
        return forms1007Signed;
    }

    public void setForms1007Signed(List<String> forms1007Signed) {
        this.forms1007Signed = forms1007Signed;
    }

    public List<String> getForm1028() {
        return form1028;
    }

    public void setForm1028(List<String> form1028) {
        this.form1028 = form1028;
    }

    public List<String> getEstimats() {
        return estimats;
    }

    public void setEstimats(List<String> estimates) {
        this.estimats = estimates;
    }

    public List<String> getBill() {
        return bill;
    }

    public void setBill(List<String> bill) {
        this.bill = bill;
    }

    public String getOpenUserSignature() {
        return openUserSignature;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }


    public String getCurrentUserName() {
        return currentUserName;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserDarga() {
        return currentUserDarga;
    }

    public String getEndCost() {
        return endCost;
    }

    public void setEndCost(String endCost) {
        this.endCost = endCost;
    }

    public String getExitDate() {
        return exitDate;
    }

    public void setExitDate(String exitDate) {
        this.exitDate = exitDate;
    }

    public String getId() {
        return String.valueOf(id);
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getGarageId() {
        return garageId;
    }

    public void setGarageId(String garageId) {
        this.garageId = garageId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getSpeedometer() {
        return speedometer;
    }

    public void setSpeedometer(String speedometer) {
        this.speedometer = speedometer;
    }

    public String getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(String fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFirstCost() {
        return firstCost;
    }

    public void setFirstCost(String firstCost) {
        this.firstCost = firstCost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBillId() {
        return billId;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public String getCloseUserSignature() {
        return closeUserSignature;
    }


    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setCurrentUserDarga(String currentUserDarga) {
        this.currentUserDarga = currentUserDarga;
    }

    public void setOpenUserSignature(String openUserSignature) {
        this.openUserSignature = openUserSignature;
    }

    public void setCloseUserName(String closeUserName) {
        this.closeUserName = closeUserName;
    }

    public void setCloseUserId(String closeUserId) {
        this.closeUserId = closeUserId;
    }

    public void setCloseUserDarga(String closeUserDarga) {
        this.closeUserDarga = closeUserDarga;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public void setCloseUserSignature(String closeUserSignature) {
        this.closeUserSignature = closeUserSignature;
    }

    public void makePdfForm(Context context) {
        try {

            PdfReader reader = new PdfReader(context.getResources().openRawResource(R.raw.form));
            PdfStamper stamper = new PdfStamper(reader,
                    new FileOutputStream(Environment.getExternalStoragePublicDirectory("//EMA" + this.id + ".pdf")));
            PdfContentByte content = stamper.getOverContent(1);
            AcroFields acroFields = stamper.getAcroFields();
            acroFields.setField("id", this.getId());
            acroFields.setField("orderId", this.getOrderId());
            acroFields.setField("garageId", this.getGarageId());
            acroFields.setField("garageName", this.getGarageName());
            acroFields.setField("equipmentType", this.getEquipmentType());
            acroFields.setField("equipmentId", this.getEquipmentId());
            acroFields.setField("repairType", this.getRepairType());
            acroFields.setField("speedometer", this.getSpeedometer());
            acroFields.setField("fuelAmount", this.getFuelAmount());
            acroFields.setField("enterDate", this.getEnterDate());
            acroFields.setField("details", this.getDetails());
            acroFields.setField("firstCost", this.getFirstCost());
            acroFields.setField("unit", "יחידה: "+this.getUnit());
            acroFields.setField("userId", this.getCurrentUserId());
            acroFields.setField("userDarga", this.getCurrentUserDarga());
            acroFields.setField("userName", this.getCurrentUserName());
            acroFields.setField("openDate", this.openDate);

            acroFields.setField("increaseUserId", this.getIncreaseUserId());
            acroFields.setField("increaseUserDarga", this.getIncreaseUserDarga());
            acroFields.setField("increaseDate", this.getIncreaseDate());
            acroFields.setField("increaseUserName", this.getIncreaseUserName());


            acroFields.setField("closeUserId", this.closeUserId);
            acroFields.setField("closeUserDarga", this.closeUserDarga);
            acroFields.setField("closeUserName", this.closeUserName);
            acroFields.setField("billId", this.billId);


            acroFields.setField("closeDate", this.closeDate);
            acroFields.setField("exitDate", this.exitDate);
            acroFields.setField("endCost", this.endCost);

            acroFields.setField("finishUserId", this.finishUserId);
            acroFields.setField("finishUserDarga", this.finishUserDarga);
            acroFields.setField("finishUserName", this.finishUserName);



            Image openUserSignature = AdapterBitmap.adaptToImage(this.getOpenUserSignature());
            addSignature(content, openUserSignature, 3, 530);
            if (this.getIncreaseUserSignature() != null) {
                Image increaseUserSignature = AdapterBitmap.adaptToImage(this.getIncreaseUserSignature());
                addSignature(content, increaseUserSignature, 3, 450);

            }

            if (this.getCloseUserSignature() != null) {
                Image closeUserSignature = AdapterBitmap.adaptToImage(this.getCloseUserSignature());
                addSignature(content, closeUserSignature, 5, 105);

            }
            if (this.getFinishUserSignature() != null) {
                acroFields.setField("billId2", this.getBillId());

                Image finishUserSignature = AdapterBitmap.adaptToImage(this.getFinishUserSignature());
                addSignature(content, finishUserSignature, 120, 30);

            }


            stamper.setFormFlattening(true);
            stamper.close();

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "EMA" + this.id + ".pdf");
            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfViewIntent.setDataAndType(photoURI, "application/pdf");


            context.startActivity(pdfViewIntent);


        } catch (IOException e) {
            e.printStackTrace();

        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }


    public void addSignature(PdfContentByte content, Image myImg, int x, int y) throws DocumentException {
        // scale the image to 50px height
        myImg.scaleAbsoluteHeight(50);
        myImg.scaleAbsoluteWidth((myImg.getWidth() * 50) / myImg.getHeight());

        myImg.setAbsolutePosition(x, y);


        System.out.println(myImg.toString());
        content.addImage(myImg);
    }

}


