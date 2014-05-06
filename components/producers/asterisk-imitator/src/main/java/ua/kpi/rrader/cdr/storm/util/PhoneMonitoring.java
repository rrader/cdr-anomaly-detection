package ua.kpi.rrader.cdr.storm.util;

public class PhoneMonitoring extends Monitoring {
    protected String phone;
    public PhoneMonitoring(String phone) {
        this.phone = phone;
    }

    @Override
    protected String getKeySuffix() {
        return ":" + phone;
    }

}
