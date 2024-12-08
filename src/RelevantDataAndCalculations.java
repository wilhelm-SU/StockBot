import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RelevantDataAndCalculations {

    String dataCSV;
    private ArrayList<DataPoint> dataPoints = new ArrayList<>();

    public RelevantDataAndCalculations(String inputDataCSV) {
        this.dataCSV = inputDataCSV;
    }

    //Input data must be formatted CSV, formatted as date, open, high, low, close/last, volume.
    public void collectCSVData() {
        String nextLine = "";
        BufferedReader dataReader;
        int dataPointIndex = 0;

        try {
            dataReader = new BufferedReader(new FileReader(dataCSV));
            dataReader.readLine();
            while ((nextLine = dataReader.readLine()) != null) {
                String[] data = nextLine.split(",");
                DataPoint inputDataPoint = new DataPoint(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]), Integer.parseInt(data[5]), 0, 0, dataPointIndex);
                dataPoints.add(0, inputDataPoint);
                dataPointIndex++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void calculateRSI() {
        int periodOfRSI = 14;

        for (int i = periodOfRSI; i < dataPoints.size(); i++) {
            double averageUp = 0;
            double averageDown = 0;

            for (int j = 1; j <= periodOfRSI; j++) {
                double change = 0;
                change = change + dataPoints.get(i - periodOfRSI + j).getOpen() - dataPoints.get(i - periodOfRSI - 1 + j).getOpen();
                if (change >= 0) {
                    averageUp = averageUp + change;
                } else {
                    averageDown = averageDown + (change * -1);
                }
            }

            averageUp = averageUp / periodOfRSI;
            averageDown = averageDown / periodOfRSI;

            if (averageDown == 0) {
                dataPoints.get(i).setRSI(100);
            } else {
                double relativeStrength = averageUp / averageDown;
                dataPoints.get(i).setRSI(100 - (100 / (1 + relativeStrength)));
            }
        }
    }

    //Uses the selected and previous 4 data point's closing number in order to determine moving average.
    public void calculateMA() {
        int periodOfMA = 5;

        for (int i = periodOfMA - 1; i < dataPoints.size(); i++) {
            double average = 0;

            for (int j = 0; j < 5; j++) {
                average = average + dataPoints.get(i - j).getOpen();
            }
            dataPoints.get(i).setMA(average/periodOfMA);
        }
    }

    public String getAllRSI() {
        String x = "";
        for (int i = 0; i < dataPoints.size(); i++) {
            x = x + dataPoints.get(i).getRSI() + ", ";
        }
        return x;
    }

    public String getAllMA(){
        String x = "";
        for (int i = 0; i < dataPoints.size(); i++) {
            x = x + dataPoints.get(i).getMA() + ", ";
        }
        return x;
    }

    public ArrayList<DataPoint> getDataPoints() {
        return dataPoints;
    }
}