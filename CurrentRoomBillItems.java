package com.company;

public class CurrentRoomBillItems {
        public String userName;
        public String iTem;
        public int iUnits;
        public int bPrice;

        public CurrentRoomBillItems(String userName, String iTem,int iUnits,int bPrice){
            this.userName = userName;
            this.iTem = iTem;
            this.iUnits = iUnits;
            this.bPrice = bPrice;
        }

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getiTem() {
            return iTem;
        }
        public void setiTem(String iTem) {
            this.iTem = iTem;
        }
        public int getiUnits() {
            return iUnits;
        }
        public void setiUnits(int iUnits) {
            this.iUnits = iUnits;
        }
        public int getbPrice() {
            return bPrice;
        }
        public void setbPrice(int bPrice) {
            this.bPrice = bPrice;
        }

        @Override
        public String toString() {
            return  "*********************************************" +
                    "\nCustomer: " + userName +
                    "\nItem: " + iTem +
                    "\nQuantity: " + iUnits +
                    "\nAmount: " + bPrice +
                    "\n*********************************************\n";

        }
}
