public enum SimulationMode {
    GameOfLife,
    Rain;

    public String toString()  {
        return switch(this) {
            case GameOfLife -> "Game Of life";
            case Rain -> "Rain";
        };
    }
}