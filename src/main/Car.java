package main;


public class Car {

    private int day;
    private String time;
    private final String licenceNumber;
    private final int personalId;
    private final int kmCounter;
    private int inOrOut;

    public Car(String[] row) {
        day = Integer.parseInt(row[0]);
        time = row[1];
        licenceNumber = row[2];
        personalId = Integer.parseInt(row[3]);
        kmCounter = Integer.parseInt(row[4]);
        inOrOut = Integer.parseInt(row[5]);

    }

    public Car(int personalId, String licenceNumber,  int kmStatus) {
        this.licenceNumber = licenceNumber;
        this.kmCounter = kmStatus;
        this.personalId=personalId;
    }

    public int getDay() {
            return day;
    }

    public String getTime() {
        return time;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public int getPersonalId() {
        return personalId;
    }

    public int getKmCounter() {
        return kmCounter;
    }

    public int getInOrOut() {
        return inOrOut;
    }

    @Override
    public String toString() {

        return day + "  " + time + "  " + licenceNumber + "  " + personalId + "  " + kmCounter + "  " + inOrOut;
    }
}
