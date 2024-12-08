import java.util.ArrayList;

public class DataPoint {
    String date;
    double open;
    double high;
    double low;
    double close;
    int volume;
    double RSI;
    double MA;
    int index;

    public DataPoint(String inputDate, double inputOpen, double inputHigh, double inputLow, double inputClose, int inputVolume, double inputRSI, double inputMA, int inputIndex){
        date = inputDate;
        open = inputOpen;
        high = inputHigh;
        low = inputLow;
        close = inputClose;
        volume = inputVolume;
        RSI = inputRSI;
        MA = inputMA;
        index = inputIndex;
    }

    //Getters
    public String getDate(){
        return date;
    }
    public double getOpen(){
        return open;
    }
    public double getHigh(){
        return high;
    }
    public double getLow(){
        return low;
    }
    public double getClose(){
        return close;
    }
    public double getRSI(){
        return RSI;
    }
    public double getMA(){
        return MA;
    }
    public int getIndex(){
        return index;
    }

    //Setters
    public void setRSI(double inputRSI){
        RSI = inputRSI;
    }
    public void setMA(double inputMA){
        MA = inputMA;
    }

    public String getAllData(){
        return getDate() + " " + getOpen() + " " + getHigh() + " " + getLow() + " " + getClose() + " " + getRSI() + " " + getMA();
    }

}
