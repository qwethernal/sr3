package librarybase;

import entity.Customer;
import entity.Product;
import entity.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class App {
    private Scanner scanner = new Scanner(System.in);
    private Scanner stringScanner = new Scanner(System.in);
    private LocalDate currentDate = LocalDate.now();
    private EntityManager em;
    
    public App(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("librarybasePU");
        this.em = emf.createEntityManager();
    }
    public void run(){
        boolean repeat = true;
        String jpqlCustomers = "SELECT c FROM Customer c";
        String jpqlProducts = "SELECT p FROM Product p";
                                    System.out.println("-----Discount date-----");
                                    System.out.print("Enter year for the start date: ");
                                    int startYear = scanner.nextInt();
                                    System.out.print("Enter month for the start date (number from 1 to 12): ");
                                    int startMonth = scanner.nextInt();
                                    System.out.print("Enter day for the start date (number from 1 to 31): ");
                                    int startDay = scanner.nextInt();
                                    LocalDate discountStartDate = LocalDate.of(startYear, startMonth, startDay);

                                    System.out.print("Enter year for the end date: ");
                                    int endYear = scanner.nextInt();
                                    System.out.print("Enter month for the end date (number from 1 to 12): ");
                                    int endMonth = scanner.nextInt();
                                    System.out.print("Enter day for the end date (number from 1 to 31): ");
                                    int endDay = scanner.nextInt();
                                    LocalDate discountEndDate = LocalDate.of(endYear, endMonth, endDay);

                                    if (currentDate.isEqual(discountStartDate) || 
                                        (currentDate.isAfter(discountStartDate) && currentDate.isBefore(discountEndDate)) ||
                                        currentDate.isEqual(discountEndDate)) {
                                        System.out.println("Today is within the discount period.");
                                    } else {
                                        System.out.println("Today is not within the discount period.");
                                    }
        
        do{
            System.out.println("Tasks: ");
            System.out.println("Select task number: ");
            System.out.println("0 - Exit");
            System.out.println("1 - Add a product");
            System.out.println("2 - List of all products");
            System.out.println("3 - Add a customer");
            System.out.println("4 - List of all customers");
            System.out.println("5 - Buy a product");
            System.out.println("6 - Total ");
            System.out.println("7 - Add money ");
            System.out.println("8 - Customers rating ");
            System.out.println("9 - Products rating ");
            System.out.println("10 - Edit customer ");
            System.out.println("11 - Edit product ");
            int task = scanner.nextInt();
            if (task == 0) {
                repeat = false;
            } else {
                System.out.println("Are you sure you want to continue with this task? (1 - yes, 0 - no)");
                int choice = scanner.nextInt();
                if (choice == 1) {
                    switch (task) {
                case 1:
                    System.out.println("-----Add a product------");
                    System.out.print("Enter the product name: ");
                    String title = stringScanner.next();
                    if (currentDate.isEqual(discountStartDate) || 
                    (currentDate.isAfter(discountStartDate) && currentDate.isBefore(discountEndDate)) ||
                    currentDate.isEqual(discountEndDate)) {
                        System.out.print("Enter the product price (the price 50% lower becaouse of discount day): ");
                    } else{
                        System.out.print("Enter the product price: ");
                    }
                    Double price = scanner.nextDouble();

                    System.out.print("Enter the product amount: ");
                    Long amount = scanner.nextLong();
                    Product product = new Product();
                    product.setTitle(title);
                    product.setPrice(price);
                    product.setAmount(amount);
                    
                    try {
                        em.getTransaction().begin();
                        if(product.getId() == null){
                            em.persist(product);
                        }else{
                            em.merge(product);
                        }
                        em.getTransaction().commit();
                    }catch (Exception e) {
                        em.getTransaction().rollback();
                    }
                    System.out.println("Product is succesfully added");
                    break;
                case 2:
                        String jpql = "SELECT p FROM Product p";
                        TypedQuery<Product> queryP = em.createQuery(jpql, Product.class);
                        List<Product> products = queryP.getResultList();
                        for (Product p : products) {
                            System.out.println(p.getId() + "." + p.toString());
                        }
                    break;
                case 3:
                    System.out.println("-----Add a customer------");
                    System.out.print("Enter the customer name: ");
                    String name = stringScanner.next();
                    System.out.print("Enter the customer balance: ");
                    Double balance = scanner.nextDouble();
                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setBalance(balance);
                                        try {
                        em.getTransaction().begin();
                        if(customer.getId() == null){
                            em.persist(customer);
                        }else{
                            em.merge(customer);
                        }
                        em.getTransaction().commit();
                    }catch (Exception e) {
                        em.getTransaction().rollback();
                    }
                    break;
                case 4:
                        String jpqls4 = "SELECT c FROM Customer c";
                        TypedQuery<Customer> queryC4 = em.createQuery(jpqls4, Customer.class);
                        List<Customer> customers = queryC4.getResultList();
                        for (Customer c : customers) {
                            System.out.println(c.getId() + "." + c.toString());
                        }
                    break;
                case 5:
                        System.out.println("-----Transaction------");
                        TypedQuery<Customer> queryCustomers = em.createQuery(jpqlCustomers, Customer.class);
                        List<Customer> allCustomers = queryCustomers.getResultList();

                        System.out.println("Choose a customer:");
                        for (Customer c : allCustomers) {
                            System.out.println(c.getId() + ". " + c.getName());
                        }

                        Long selectedCustomerId = scanner.nextLong();

                        TypedQuery<Product> queryProducts = em.createQuery(jpqlProducts, Product.class);
                        List<Product> allProducts = queryProducts.getResultList();

                        System.out.println("Choose a product:");
                        for (Product p : allProducts) {
                            System.out.println(p.getId() + ". " + p.getTitle());
                        }

                        Long selectedProductId = scanner.nextLong();
                        Customer selectedCustomer = em.find(Customer.class, selectedCustomerId);
                        Product selectedProduct = em.find(Product.class, selectedProductId);
                        if (selectedCustomer == null) {
                            System.out.println("Customer not found");
                            break;
                        }
                        if (selectedProduct == null) {
                            System.out.println("Product not found");
                            break;
                        }

                        System.out.println("Enter the quantity to buy:");
                        Long quantityToBuy = scanner.nextLong();
                        if (quantityToBuy > selectedProduct.getAmount()) {
                            System.out.println("Not enough quantity of the product");
                            break;
                        }
                        double totalCost = selectedProduct.getPrice() * quantityToBuy;
                        if (totalCost > selectedCustomer.getBalance()) {
                            System.out.println("Not enough balance");
                            break;
                        }

                        try {
                            em.getTransaction().begin();
                            selectedCustomer.setBalance(selectedCustomer.getBalance() - totalCost);
                            selectedProduct.setAmount(selectedProduct.getAmount() - quantityToBuy);
                            Transaction transaction = new Transaction();
                            transaction.setAmount(totalCost);
                            transaction.setCustomer(selectedCustomer);
                            transaction.setProduct(selectedProduct);
                            em.persist(transaction);
                            em.getTransaction().commit();

                            System.out.println("Transaction completed successfully");
                        } catch (Exception e) {
                            em.getTransaction().rollback();
                            System.out.println("Transaction failed: " + e.getMessage());
                        }

                        break;

                case 6:
                            String jpqlTotal = "SELECT SUM(t.amount) FROM Transaction t";
                            TypedQuery<Double> queryTotal = em.createQuery(jpqlTotal, Double.class);
                            Double totalAmount = queryTotal.getSingleResult();
                            System.out.println("Total amount of all purchases: " + totalAmount);
                            break;
                case 7:
                            TypedQuery<Customer> queryCustomers7 = em.createQuery(jpqlCustomers, Customer.class);
                            List<Customer> allCustomers7 = queryCustomers7.getResultList();
                            System.out.println("Choose a customer:");
                            for (Customer c : allCustomers7) {
                                System.out.println(c.getId() + ". " + c.getName());
                            }
                            Long selectedCustomerId7 = scanner.nextLong();
                            Customer selectedCustomer7 = em.find(Customer.class, selectedCustomerId7);
                            if (selectedCustomer7 == null) {
                                System.out.println("Customer not found");
                                break;
                            }
                            System.out.println("How much money you want to add: ");
                            Float additionalBalance = scanner.nextFloat();
                            selectedCustomer7.setBalance(selectedCustomer7.getBalance() + additionalBalance);
                            try {
                                em.getTransaction().begin();
                                em.merge(selectedCustomer7);
                                em.getTransaction().commit();
                                System.out.println("Successfully added");
                            } catch (Exception e) {
                                em.getTransaction().rollback();
                                System.out.println("Failed to add money: " + e.getMessage());
                            }

                            break;
                case 8:
                                String jpqlRanking = "SELECT t.customer, SUM(t.amount) FROM Transaction t GROUP BY t.customer ORDER BY SUM(t.amount) DESC";
                                TypedQuery<Object[]> queryRanking = em.createQuery(jpqlRanking, Object[].class);
                                List<Object[]> rankingResults = queryRanking.getResultList();
                                System.out.println("Customer Spending Rating:");
                                int rank = 1;
                                for (Object[] result : rankingResults) {
                                    Customer rankedCustomer = (Customer) result[0];
                                    double totalSpent = (double) result[1];
                                    System.out.println(rank + ". " + rankedCustomer.getName() + ": €" + totalSpent);
                                    rank++;
                                }
                                break;
                case 9:
                                String jpqlProductRanking = "SELECT t.product, SUM(t.amount) FROM Transaction t GROUP BY t.product ORDER BY SUM(t.amount) DESC";
                                TypedQuery<Object[]> queryProductRanking = em.createQuery(jpqlProductRanking, Object[].class);
                                List<Object[]> productRankingResults = queryProductRanking.getResultList();
                                System.out.println("Products Sales Rating:");
                                for (int i = 0; i < productRankingResults.size(); i++) {
                                    Object[] result = productRankingResults.get(i);
                                    Product rankingProduct = (Product) result[0];
                                    Double totalUnitsSold = (Double) result[1];
                                    System.out.println((i + 1) + ". " + rankingProduct.getTitle() + " - Sold " + totalUnitsSold + " units");
                                }
                                break;
                case 10:
                                TypedQuery<Customer> queryCustomers10 = em.createQuery(jpqlCustomers, Customer.class);
                                List<Customer> allCustomers10 = queryCustomers10.getResultList();
                                System.out.println("Choose a customer to edit:");
                                for (Customer c : allCustomers10) {
                                    System.out.println(c.getId() + ". " + c.getName());
                                }
                                Long selectedCustomerId10 = scanner.nextLong();
                                Customer selectedCustomer10 = em.find(Customer.class, selectedCustomerId10);
                                if (selectedCustomer10 == null) {
                                    System.out.println("Customer not found");
                                    break;
                                }
                                System.out.println("Enter new balance for that customer");

                                Double newBalance = scanner.nextDouble();
                                selectedCustomer10.setBalance(newBalance);
                            break;

                case 11:
                                System.out.println("Choose a product:");
                                TypedQuery<Product> queryProducts11 = em.createQuery(jpqlProducts, Product.class);
                                List<Product> allProducts11 = queryProducts11.getResultList();
                                for (Product p : allProducts11) {
                                    System.out.println(p.getId() + ". " + p.getTitle());
                                }
                                Long selectedProductId11 = scanner.nextLong();
                                Product selectedProduct11 = em.find(Product.class, selectedProductId11);
                                if (selectedProduct11 == null) {
                                    System.out.println("Product not found");
                                    break;
                                }
                                System.out.println("Choose what you want to edit:");
                                System.out.println("1 - Edit amount");
                                System.out.println("2 - Edit price");
                                int choice11 = scanner.nextInt();

                                switch(choice11){
                                    case 1:
                                        System.out.println("Enter new amount:(last was: " + selectedProduct11.getAmount() + ")");
                                        Long newAmount = scanner.nextLong();
                                        selectedProduct11.setAmount(newAmount);
                                        break;
                                    case 2:
                                        System.out.println("Enter new price: (last was: " + selectedProduct11.getPrice() + ")");
                                        Double newPrice = scanner.nextDouble();
                                        selectedProduct11.setPrice(newPrice);
                                        break;
                                    default:
                                        System.out.println("Invalid choice");
                                        break;
                                }
                                   try {
                                    em.getTransaction().begin();
                                    em.merge(selectedProduct11);
                                    em.getTransaction().commit();
                                    System.out.println("Product information updated successfully");
                                } catch (Exception e) {
                                    em.getTransaction().rollback();
                                    System.out.println("Failed to update product information: " + e.getMessage());
                                }
                                   break;
                        default:
                            System.out.println("Invalid task number. Please choose a valid task.");
                            break;
                    }
                }
            }
        } while (repeat);
    }
}