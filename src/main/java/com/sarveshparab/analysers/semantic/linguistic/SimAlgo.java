package com.sarveshparab.analysers.semantic.linguistic;

public enum SimAlgo {
    WuPalmer(0),
    Resnik(1),
    JiangConrath(2),
    Lin(3),
    LeacockChodorow(4),
    Path(5),
    Lesk(6),
    HirstStOnge(7);

    private int index;

    SimAlgo(int index) {
        this.index = index;
    }

    public int useAlgo(){
        return index;
    }
}
