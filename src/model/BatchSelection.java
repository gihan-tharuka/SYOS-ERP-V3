package model;

public class BatchSelection {
    private MainStock batch;
    private int reshelfQuantity;

    public BatchSelection(MainStock batch, int reshelfQuantity) {
        this.batch = batch;
        this.reshelfQuantity = reshelfQuantity;
    }

    public MainStock getBatch() {
        return batch;
    }

    public int getReshelfQuantity() {
        return reshelfQuantity;
    }
}

