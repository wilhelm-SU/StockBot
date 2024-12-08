
public class StockBot {

    double startingBalance;
    double currentBalance;
    double investedBalance = 0;
    int shares = 0;
    int startingPeriod = 14;
    RelevantDataAndCalculations stock;

    public StockBot(double inputStartingBalance, RelevantDataAndCalculations inputStock) {
        startingBalance = inputStartingBalance;
        currentBalance = startingBalance;
        stock = inputStock;
    }

    public void stockSimulation(int algoNum) {
        stock.collectCSVData();
        stock.calculateRSI();
        stock.calculateMA();

        int timeFrame = stock.getDataPoints().size();

        System.out.println("Time frame: " + timeFrame);
        System.out.println("Starting balance: " + currentBalance);

        int i = startingPeriod;
        while (i < timeFrame) {

            DataPoint currentPoint = stock.getDataPoints().get(i);
            System.out.println(stock.getDataPoints().get(i).getDate());

            if (algoNum == 1) {
                tradeEvaluator(algorithmOne(currentPoint), currentPoint);
            } else if (algoNum == 2) {
                tradeEvaluator(algorithmTwo(currentPoint), currentPoint);
            } else if (algoNum == 3) {
                tradeEvaluator(algorithmThree(currentPoint), currentPoint);
            }

            System.out.println("Invested balance: " + investedBalance);
            System.out.println("Shares: " + shares);
            System.out.println();
            i++;
        }
        System.out.println("Starting balance: " + startingBalance);
        System.out.println("Current balance: " + currentBalance);
    }

    /**
     *
     * @param inputAlgorithm the specific algorithm which will be used to actually determine when to buy, sell and hold
     * @param currentPoint the current data point in which the method is evaluating
     */
    public void tradeEvaluator(int inputAlgorithm, DataPoint currentPoint) {
        double beforeSellingInvestedBalance = shares * currentPoint.getOpen();
        shares = shares + inputAlgorithm;
        investedBalance = shares * currentPoint.getOpen();

        if (inputAlgorithm < 0) {
            currentBalance = currentBalance + (beforeSellingInvestedBalance - investedBalance);
            System.out.println("Current Balance: " + currentBalance);
        }
        if (inputAlgorithm >= 0) {
            currentBalance = currentBalance - inputAlgorithm * currentPoint.getOpen();
            if (inputAlgorithm > 0) {
                System.out.println("Current Balance: " + currentBalance);
            }
        }
        if (currentPoint.getDate().equals(stock.getDataPoints().get(stock.getDataPoints().size() - 1).getDate())) {
            //Selling all shares at the end of time frame
            currentBalance = currentBalance + investedBalance;
            shares = 0;
        }
    }

    /**
     * Simple algorithm which buys a set amount of Stock at the beginning of the time frame, and sells it all at the end
     *
     * @param currentPoint the current data point in which the method is evaluating
     * @return returns the amount of shares to be bought, sold or held. Positive means buy, negative means sell, 0 means hold
     */
    public int algorithmOne(DataPoint currentPoint) {
        int sharesToBuyOrSell = 0;

        if (currentPoint.getDate().equals(stock.getDataPoints().get(startingPeriod).getDate())) {
            sharesToBuyOrSell = 500;
        }
        if (currentPoint.getDate().equals(stock.getDataPoints().get(stock.getDataPoints().size() - 1).getDate())) {
            sharesToBuyOrSell = -500;
        }

        return sharesToBuyOrSell;
    }

    /**
     *
     * @param currentPoint
     * @return
     */
    public int algorithmTwo(DataPoint currentPoint) {
        int sharesToBuyOrSell = 0;

        if (currentPoint.getRSI() >= 90 && shares > 0 && currentPoint.getMA() > currentPoint.getOpen()) {
            sharesToBuyOrSell = -1 * shares;
        } else if ((currentPoint.getRSI() >= 80 && currentPoint.getMA() < currentPoint.getOpen()) && shares > 30) {
            sharesToBuyOrSell = -30;
        } else if ((currentPoint.getRSI() >= 70 && currentPoint.getMA() < currentPoint.getOpen()) && shares > 10) {
            sharesToBuyOrSell = -10;
        } else if ((currentPoint.getRSI() <= 30 && currentPoint.getMA() > currentPoint.getOpen()) && shares < 150 && currentBalance > currentPoint.getOpen() * 10) {
            sharesToBuyOrSell = 15;
        } else if ((currentPoint.getRSI() <= 20 && currentPoint.getMA() > currentPoint.getOpen()) && shares < 300 && currentBalance > currentPoint.getOpen() * 50) {
            sharesToBuyOrSell = 30;
        } else if ((currentPoint.getRSI() <= 10 && currentPoint.getMA() > currentPoint.getOpen()) && shares < 500 && currentBalance > currentPoint.getOpen() * 100) {
            sharesToBuyOrSell = 100;
        }
        return sharesToBuyOrSell;
    }


    public int algorithmThree(DataPoint currentPoint) {
        int sharesToBuyOrSell = 0;

        //If the last stock sold was more than what we want to buy today with 25% room for fluctuation, we don't buy
        if(!(currentPoint.getIndex() > 1 && (currentPoint.getOpen() + currentPoint.getOpen()*.25 < stock.getDataPoints().get(currentPoint.getIndex() - 1).getOpen()))) {
            if ((currentPoint.getRSI() >= 80 && currentPoint.getMA() < currentPoint.getOpen()) && shares > 30) {
                sharesToBuyOrSell = -30;
            } else if ((currentPoint.getRSI() >= 70 && currentPoint.getMA() < currentPoint.getOpen()) && shares > 15) {
                sharesToBuyOrSell = -15;
            }
        }

        //If the current index is near the last 30 indexes of data, will stop buying and only sell if RSI permits
        if (currentPoint.getIndex() >= stock.getDataPoints().size() - 15) {
            return sharesToBuyOrSell;
        }

        //If the last stock purchased was less than what we want to buy today with 25% room for fluctuation, we don't sell
        if(!(currentPoint.getIndex() > 1 && (currentPoint.getOpen() > stock.getDataPoints().get(currentPoint.getIndex() - 1).getOpen() + stock.getDataPoints().get(currentPoint.getIndex() - 1).getOpen() * .25))) {
            if ((currentPoint.getRSI() <= 30 && currentPoint.getMA() > currentPoint.getOpen()) && shares < 150 && currentBalance > currentPoint.getOpen() * 10) {
                sharesToBuyOrSell = 15;
            } else if ((currentPoint.getRSI() <= 20 && currentPoint.getMA() > currentPoint.getOpen()) && shares < 200 && currentBalance > currentPoint.getOpen() * 50) {
                sharesToBuyOrSell = 20;
            } else if ((currentPoint.getRSI() <= 10 && currentPoint.getMA() > currentPoint.getOpen()) && shares < 300 && currentBalance > currentPoint.getOpen() * 100) {
                sharesToBuyOrSell = 30;
            }
        }
        return sharesToBuyOrSell;
    }
}
