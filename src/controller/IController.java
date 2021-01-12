package controller;

public interface IController {

        boolean CheckScanCode(String inScanCode);

        void AddItemToList(String text);

        void UpdateCheckoutList(String text);
}
