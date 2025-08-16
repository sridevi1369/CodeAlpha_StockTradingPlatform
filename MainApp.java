import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    void updatePrice(double newPrice) {
        this.price = newPrice;
    }
}

class Portfolio {
    Map<String, Integer> ownedStocks = new HashMap<>();
    double cashBalance = 10000.0; // starting cash

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cost > cashBalance) {
            System.out.println("Not enough cash. Needed $" + cost + ", available $" + cashBalance);
            return;
        }
        ownedStocks.put(stock.symbol, ownedStocks.getOrDefault(stock.symbol, 0) + quantity);
        cashBalance -= cost;
        System.out.println("✅ Bought " + quantity + " " + stock.symbol + " @ $" + stock.price + " | Cash left: $" + cashBalance);
    }

    void sellStock(Stock stock, int quantity) {
        if (!ownedStocks.containsKey(stock.symbol) || ownedStocks.get(stock.symbol) < quantity) {
            System.out.println("Not enough shares to sell.");
            return;
        }
        ownedStocks.put(stock.symbol, ownedStocks.get(stock.symbol) - quantity);
        if (ownedStocks.get(stock.symbol) == 0) {
            ownedStocks.remove(stock.symbol);
        }
        double revenue = stock.price * quantity;
        cashBalance += revenue;
        System.out.println("✅ Sold " + quantity + " " + stock.symbol + " @ $" + stock.price + " | Cash now: $" + cashBalance);
    }

    void viewPortfolio(Map<String, Stock> market) {
        System.out.println("\n---------- PORTFOLIO ----------");
        if (ownedStocks.isEmpty()) {
            System.out.println("No stocks owned.");
        } else {
            double totalValue = 0;
            for (String symbol : ownedStocks.keySet()) {
                int qty = ownedStocks.get(symbol);
                double currentPrice = market.get(symbol).price;
                double value = qty * currentPrice;
                totalValue += value;
                System.out.println(symbol + " | Qty: " + qty + " | Current Price: $" + currentPrice + " | Value: $" + value);
            }
            System.out.println("Cash Balance: $" + cashBalance);
            System.out.println("Total Portfolio Value: $" + (cashBalance + totalValue));
        }
        System.out.println("--------------------------------");
    }
}

class StockMarket {
    Map<String, Stock> stocks = new HashMap<>();
    Random rand = new Random();

    StockMarket() {
        stocks.put("AAPL", new Stock("AAPL", "Apple", 150));
        stocks.put("GOOG", new Stock("GOOG", "Google", 2800));
        stocks.put("TSLA", new Stock("TSLA", "Tesla", 700));
        stocks.put("MSFT", new Stock("MSFT", "Microsoft", 320));
        stocks.put("NFLX", new Stock("NFLX", "Netflix", 420));
    }

    void displayMarketData() {
        System.out.println("\n------------- MARKET -------------");
        System.out.printf("%-6s %-15s %-10s\n", "SYM", "NAME", "PRICE");
        for (Stock s : stocks.values()) {
            System.out.printf("%-6s %-15s $%.2f\n", s.symbol, s.name, s.price);
        }
        System.out.println("----------------------------------");
    }

    void updatePricesRandomly() {
        for (Stock s : stocks.values()) {
            double change = rand.nextDouble() * 10 - 5; // -5 to +5
            s.updatePrice(Math.round((s.price + change) * 100.0) / 100.0);
        }
        System.out.println("📈 Prices updated!\n");
    }
}

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StockMarket market = new StockMarket();
        Portfolio portfolio = new Portfolio();

        while (true) {
            market.displayMarketData();
            System.out.println("\nMENU:");
            System.out.println("1) Buy Stock");
            System.out.println("2) Sell Stock");
            System.out.println("3) View Portfolio");
            System.out.println("4) Next Price Tick");
            System.out.println("5) Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = sc.next().toUpperCase();
                    if (market.stocks.containsKey(buySymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        portfolio.buyStock(market.stocks.get(buySymbol), qty);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = sc.next().toUpperCase();
                    if (market.stocks.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity: ");
                        int sellQty = sc.nextInt();
                        portfolio.sellStock(market.stocks.get(sellSymbol), sellQty);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 3:
                    portfolio.viewPortfolio(market.stocks);
                    break;

                case 4:
                    market.updatePricesRandomly();
                    break;

                case 5:
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}