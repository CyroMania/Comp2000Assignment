package model;

import java.util.*;

public class Stock extends Model{

        private List<Item> products;
        private Integer minQuantity;




        public Stock(Integer inMinQuantity) {
                minQuantity = inMinQuantity;
        }


        public void AddItem(Item inItem) {
                this.products.add(inItem);
        }

        public boolean CheckItemExists(String inScanCode) {
                for (Item product : products) {
                        if (product.getScanCode() == inScanCode) {
                                return true;
                        }
                }
                return false;
        }

        public List<Item> getProducts() {
                return products;
        }

        public Integer getMinQuantity() {
                return minQuantity;
        }

}
