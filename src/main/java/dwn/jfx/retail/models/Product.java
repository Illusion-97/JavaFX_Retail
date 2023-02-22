package dwn.jfx.retail.models;

public enum Product implements Merch {
    EXEMPLES {
        @Override
        public String getName() {
            return "EXEMPLES";
        }

        @Override
        public double getPrice() {
            return 0.5;
        }
    }
}
