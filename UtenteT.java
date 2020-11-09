public class UtenteT {
    //Attributi
    private long idUtente;
    private StatoGioco stato = StatoGioco.INCORSO;
    private char[][] matrice = {{'⬜', '⬜', '⬜'}, {'⬜', '⬜', '⬜'},{'⬜', '⬜','⬜'}};

    //X: ❌   O: ⭕     Espacio: ⬜

    //Costruttore
    public UtenteT(long idUtente){
        this.idUtente = idUtente;
    }

    //Metodi
    public void playUtente(int row, int col){
        this.matrice[row][col] = '❌';
    }


    public long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(long idUtente) {
        this.idUtente = idUtente;
    }

    public StatoGioco getStato() {
        return stato;
    }

    public void setStato(StatoGioco stato) {
        this.stato = stato;
    }

    public char[][] getMatrice() {
        return matrice;
    }

    public void setMatrice(char[][] matrice) {
        this.matrice = matrice;
    }
}
