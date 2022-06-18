package main;

import java.io.*;
import java.util.*;

public class Main {

    private final static List<Car> CARS = new ArrayList<>();
    static String path = "autok.txt";
    static Scanner scanner = new Scanner(System.in);
    static String gotLicenceNumber;
    static Map<String, Integer> listOfUniqueCars = new HashMap<>();
    static Map<String, Integer> carsKmStatisticsList = new HashMap<>();

    public static void main(String[] args) {
        fileReading();

        System.out.println("              ****             ");
        System.out.println("2. feladat:");
        Car lastTakenCar = lastCarTaken();
        assert lastTakenCar != null;
        System.out.println("Az utoljára elvitt autó: " + lastTakenCar.getDay() + ". napon a "
                + lastTakenCar.getLicenceNumber() + " rendszámú autó volt.");

        //3.feladat
        displayCarsTrafficOnAGivenDay();

        System.out.println("              ****             ");

        System.out.println("4. feladat");
        System.out.println("A hónap végén " + missedCarsAtTheEndOfMonth() + " autót nem hoztak vissza.");

        System.out.println("              ****             ");

        System.out.println("5. feladat: ");
        System.out.println("A gépjárművek a következő távolságokat tették meg a vizsgált hónapban:");
        carsKmStatistics();
        for (String car : carsKmStatisticsList.keySet()) {
            System.out.println(car + " " + carsKmStatisticsList.get(car) + " km");
        }

        System.out.println("              ****             ");

        System.out.println("6. feladat: ");
        Car theMostTakenRoute = theMostKmTakenByAPerson();
        System.out.println("A leghosszabb út: " + theMostTakenRoute.getKmCounter() + " km, személy: "
                + theMostTakenRoute.getPersonalId());
        itineraryMagic();

        System.out.println("              ****             ");

        System.out.println("7. feladat: ");
        System.out.println("Rendszám: " + gotLicenceNumber);
        System.out.println("A menetlevél kész.");
    }


    private static void fileReading() {
        String inputRow;
        String[] tempArray;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);

            while ((inputRow = reader.readLine()) != null) {
                tempArray = inputRow.split(" ");
                CARS.add(new Car(tempArray));
            }
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Car lastCarTaken() {
        for (int i = CARS.size() - 1; i >= 0; i--) {
            if (CARS.get(i).getInOrOut() == 0) {
                return CARS.get(i);
            }
        }
        return null;
    }


    private static void displayCarsTrafficOnAGivenDay() {
        List<Car> lookedForCars = new ArrayList<>();
        takenCarsOnAGivenDay(lookedForCars);
        String inOrOut;
        System.out.println("              ****             ");
        System.out.println("3. feladat:");
        System.out.println("Nap: " + lookedForCars.get(0).getDay());
        System.out.println("Forgalom a(z) " + lookedForCars.get(0).getDay() + ". napon:");
        for (Car lookedForCar : lookedForCars) {
            System.out.print(lookedForCar.getTime() + " " + lookedForCar.getLicenceNumber() + " "
                    + lookedForCar.getPersonalId());
            inOrOut = lookedForCar.getInOrOut() == 0 ? " ki" : " be";
            System.out.println(inOrOut);
        }
    }


    private static void takenCarsOnAGivenDay(List<Car> lookedForCars) {
        int givenDay = dayRequest();
        for (Car car : CARS) {
            if (car.getDay() == givenDay) {
                lookedForCars.add(car);
            }
        }

    }


    private static int dayRequest() {
        int givenDay = 0;
        do {
            try {
                System.out.print("Kérem, hogy adja meg a hónap egy napját 1-30-ig: ");
                givenDay = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Nem számot adott meg. Kérem adja meg újra.");
                scanner.nextLine();
            }
        } while (givenDay > 30 || givenDay < 1);

        return givenDay;
    }


    private static int missedCarsAtTheEndOfMonth() {
        makeUniqueCarsWithStatus();
        int numberOfMissedCars = 0;
        for (String s : listOfUniqueCars.keySet()) {
            if (listOfUniqueCars.get(s) == 0) {
                numberOfMissedCars++;
            }
        }
        return numberOfMissedCars;
    }


    private static void makeUniqueCarsWithStatus() {
        for (Car car : CARS) {
            listOfUniqueCars.put(car.getLicenceNumber(), car.getInOrOut());
        }
    }

    private static void carsKmStatistics() {
        int smallestKm;
        int largestKm;
        for (String key : listOfUniqueCars.keySet()) {
            smallestKm = 0;
            largestKm = 0;
            for (Car car : CARS) {
                if (car.getLicenceNumber().equals(key) && smallestKm == 0 && largestKm == 0) {
                    largestKm = car.getKmCounter();
                    smallestKm = car.getKmCounter();
                } else if (car.getLicenceNumber().equals(key) && car.getKmCounter() < smallestKm) {
                    smallestKm = car.getKmCounter();
                } else if (car.getLicenceNumber().equals(key) && car.getKmCounter() > largestKm) {
                    largestKm = car.getKmCounter();
                }
            }
            carsKmStatisticsList.put(key, largestKm - smallestKm);
        }
    }


    private static Car theMostKmTakenByAPerson() {
        Map<Integer, Integer> personList = makePersonList();

        List<Car> takenKmsByPersonByCar = new ArrayList<>();
        int startKm = 0;
        int finishKm;
        String licenceNumber = null;

        for (Integer personId : personList.keySet()) {
            for (Car car : CARS) {
                if (car.getPersonalId() == personId && car.getInOrOut() == 0) {
                    startKm = car.getKmCounter();
                    licenceNumber = car.getLicenceNumber();

                } else if (car.getPersonalId() == personId && car.getLicenceNumber().equals(licenceNumber) && car.getInOrOut() == 1) {
                    finishKm = car.getKmCounter();
                    takenKmsByPersonByCar.add(new Car(personId, car.getLicenceNumber(), finishKm - startKm));
                    startKm = 0;
                    finishKm = 0;
                    licenceNumber = null;
                }
            }
        }
        sortByKmStatusDesc(takenKmsByPersonByCar);
        return takenKmsByPersonByCar.get(0);
    }

    private static Map<Integer, Integer> makePersonList() {
        Map<Integer, Integer> personList = new HashMap<>();
        for (Car car : CARS) {
            personList.put(car.getPersonalId(), 0);
        }
        return personList;
    }


    private static void sortByKmStatusDesc(List<Car> takenKmsByPersonByCar) {
        takenKmsByPersonByCar.sort(new Comparator<Car>() {
            @Override
            public int compare(Car o1, Car o2) {
                if (o1.getKmCounter() < o2.getKmCounter()) {
                    return 1;
                } else if (o1.getKmCounter() > o2.getKmCounter()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }


    private static void itineraryMagic() {
        String licenceNumberFromUser = getLicenceNumberFromUserToItineraryMagic();
        List<Car> licenceNumberTraffic = makeTrafficByCar(licenceNumberFromUser);
        String[] separator = {System.lineSeparator(), ""};
        try {
            FileWriter fileWriter = new FileWriter(licenceNumberFromUser + "_menetlevel.txt");
            for (int i = 0, j = i + 1; i < licenceNumberTraffic.size(); i += 2, j += 2) {
                fileWriter.append(String.valueOf(licenceNumberTraffic.get(i).getPersonalId()));
                fileWriter.append("\t");
                fileWriter.append(" start ");
                fileWriter.append("day: ");
                fileWriter.append(String.valueOf(licenceNumberTraffic.get(i).getDay()));
                fileWriter.append("\t");
                fileWriter.append(String.valueOf(licenceNumberTraffic.get(i).getTime()));
                fileWriter.append("\t");
                fileWriter.append(String.valueOf(licenceNumberTraffic.get(i).getKmCounter()));
                fileWriter.append(" km");
                if (j < licenceNumberTraffic.size()) {
                    fileWriter.append("\t");
                    fileWriter.append(" finish");
                    fileWriter.append(" day: ");
                    fileWriter.append(String.valueOf(licenceNumberTraffic.get(j).getDay()));
                    fileWriter.append("\t");
                    fileWriter.append(String.valueOf(licenceNumberTraffic.get(j).getTime()));
                    fileWriter.append("\t");
                    fileWriter.append(String.valueOf(licenceNumberTraffic.get(j).getKmCounter()));
                    fileWriter.append(" km");
                    fileWriter.append(separator[j / (licenceNumberTraffic.size() - 1)]);
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLicenceNumberFromUserToItineraryMagic() {
        do {
            System.out.print("Kérek egy rendszámot (ABC123) formátumban: ");
            gotLicenceNumber = scanner.nextLine();
        } while (!isLicenceNumberValid(gotLicenceNumber));

        return gotLicenceNumber;
    }

    private static boolean isLicenceNumberValid(String gotLicenceNumber) {

        for (String car : carsKmStatisticsList.keySet()) {
            if (car.equals(gotLicenceNumber)) {
                return true;
            }
        }
        return false;
    }


    private static List<Car> makeTrafficByCar(String gotLicenceNumberFromUser) {
        List<Car> trafficByCars = new ArrayList<>();
        for (Car car : CARS) {
            if (car.getLicenceNumber().equals(gotLicenceNumberFromUser)) {
                trafficByCars.add(car);
            }
        }
        return trafficByCars;
    }


}




