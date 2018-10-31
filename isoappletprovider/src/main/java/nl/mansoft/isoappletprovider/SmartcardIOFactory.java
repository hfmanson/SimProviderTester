package nl.mansoft.isoappletprovider;

public class SmartcardIOFactory {
    public static BaseSmartcardIO getInstance() {
        return IccSmartcardIO.getInstance();
    }
}
