public class Tester {
    public static void main(String[] args) {

        RelevantDataAndCalculations testStock = new RelevantDataAndCalculations("src/AMDHistoricalData.csv");
        StockBot test1 = new StockBot(25000, testStock);
        StockBot test2 = new StockBot(25000, testStock);
        StockBot test3 = new StockBot(25000, testStock);

      //  test1.stockSimulation(1);
       // test2.stockSimulation(2);
        test3.stockSimulation(3);

    }

}
