package com.example.ema.classs;

public class RepairType {
    private String repairCode;
    private String repairDeatail;


    public RepairType(String repairCode, String repairDeatail) {
        this.repairCode = repairCode;
        this.repairDeatail = repairDeatail;
    }

    public String getRepairCode() {
        return repairCode;
    }

    public void setRepairCode(String repairCode) {
        this.repairCode = repairCode;
    }

    public String getRepairDeatail() {
        return repairDeatail;
    }

    public void setRepairDeatail(String repairDeatail) {
        this.repairDeatail = repairDeatail;
    }
}
