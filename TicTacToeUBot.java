import com.botticelli.bot.Bot;
import com.botticelli.bot.request.methods.MessageToSend;
import com.botticelli.bot.request.methods.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeUBot extends Bot {
    //Attributi
    ArrayList<UtenteT> listaUtenti;

    //Costruttore
    public TicTacToeUBot(String token) {
        super(token);
        listaUtenti = new ArrayList<>();
    }

    //Metodi

    public void printTastiera(long playerId){
        List<KeyboardButton> line = new ArrayList<>();
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        //Primera linea
        line.add(new KeyboardButton("00"));
        line.add(new KeyboardButton("01"));
        line.add(new KeyboardButton("02"));
        keyboard.add(line);

        //Segunda linea
        line = new ArrayList<>();
        line.add(new KeyboardButton("10"));
        line.add(new KeyboardButton("11"));
        line.add(new KeyboardButton("12"));
        keyboard.add(line);

        //Tercera linea
        line = new ArrayList<>();
        line.add(new KeyboardButton("20"));
        line.add(new KeyboardButton("21"));
        line.add(new KeyboardButton("22"));
        keyboard.add(line);

        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);

        MessageToSend mts = new MessageToSend(playerId, "----------------------");
        mts.setReplyMarkup(replyKeyboard);
        sendMessage(mts);

    }

    public void printMatrice(long playerId, char[][] matrice){
        MessageToSend mts = new MessageToSend(playerId,  matrice[0][0] + " | " + matrice[0][1] + " | " + matrice[0][2] +"\n" +
                matrice[1][0] + " | " + matrice[1][1] + " | " + matrice[1][2] +"\n" +
                matrice[2][0] + " | " + matrice[2][1] + " | " + matrice[2][2] +"\n");

        sendMessage(mts);

    }

    public boolean isPosAvailable(char[][] matrice, int row, int col){
        if(matrice[row][col] == '⬜'){
            return true;
        }
        return false;
    }

    public boolean isMatriceFull(char[][] matrice, UtenteT utente){
        boolean isThereSpace = false;
        for (int row = 0; row < matrice.length; row++) {
            for (int col = 0; col < matrice.length; col++) {
                if(matrice[row][col] == '⬜'){
                    isThereSpace = true;
                    break;
                }
            }
        }

        if(!isThereSpace){
            utente.setStato(StatoGioco.TERMINATO);
            return true;
        }
        return false;
    }

    public int[] playBot(char[][] matrice){
        Random random = new Random();
        int[] arrayPos = new int[2];
        int row;
        int col;

        do{
            row = random.nextInt(matrice.length);
            col = random.nextInt(matrice.length);

        } while (!isPosAvailable(matrice, row, col));

        matrice[row][col] = '⭕';
        arrayPos[0] = row;
        arrayPos[1] = col;

        return arrayPos;
    }

    public boolean didYouWin(char[][] matrice, int row, int col, char charInput, UtenteT utente){

        //check horizontal
        if(col == 0){
            if(matrice[row][col+1] == charInput && matrice[row][col+2] == charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }

        if(col == 1){
            if(matrice[row][col-1] == charInput && matrice[row][col+1] == charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }

        if(col == 2){
            if(matrice[row][col-2] == charInput && matrice[row][col-1] == charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }

        //Check vertical
        if(row==0){
            if(matrice[row+1][col] == charInput && matrice[row+2][col] == charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }
        if(row ==1){
            if(matrice[row-1][col] == charInput && matrice[row+1][col] == charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }
        if(row == 2){
            if(matrice[row-2][col] == charInput && matrice[row-1][col]== charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }

        //check diagonal principal
        if(row==col){
            if(matrice[0][0] == charInput && matrice[1][1] == charInput && matrice[2][2] == charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }
        //Check diagonal inv
        if(row + col == matrice.length-1){
            if(matrice[0][matrice.length-1] == charInput && matrice[1][1] == charInput && matrice[matrice.length-1][0] == charInput){
                System.out.println( charInput + " ha vinto!");
                utente.setStato(StatoGioco.TERMINATO);
                return true;
            }
        }

        return false;
    }



    @Override
    public void textMessage(Message message) {

        boolean isThereUtente = false;
        UtenteT utenteAtt = null;
        for(UtenteT u : listaUtenti){
            if(message.getFrom().getId() == u.getIdUtente()){
                isThereUtente = true;
                utenteAtt = u;
            }
        }
        if(!isThereUtente){
            utenteAtt = new UtenteT(message.getFrom().getId());
            listaUtenti.add(utenteAtt);

        }

        printTastiera(message.getFrom().getId());
        System.out.println(message.getText());

        if(message.getText().equals("/start")){
            utenteAtt.setMatrice(new char[][]{{'⬜', '⬜', '⬜'}, {'⬜', '⬜', '⬜'},{'⬜', '⬜','⬜'}});
            printMatrice(message.getFrom().getId(), utenteAtt.getMatrice());
            utenteAtt.setStato(StatoGioco.INCORSO);
            MessageToSend mts = new MessageToSend(message.getFrom().getId(), "You go first X!");
            sendMessage(mts);


        } else{
            if(utenteAtt.getStato() == StatoGioco.INCORSO){
                //Agarraro row y col donde el player quiere jugar del mensaje
                int row = Character.getNumericValue(message.getText().charAt(0));
                int col = Character.getNumericValue(message.getText().charAt(1));

                System.out.println("row: " + row + " col: " + col);
                if(isPosAvailable(utenteAtt.getMatrice(), row, col)){
                    //Si la posicion esta disponible, pongo la X del jugador ahi
                    utenteAtt.playUtente(row, col);

                    //Muestro la matriz
                    printMatrice(message.getFrom().getId(), utenteAtt.getMatrice());

                    //Verifico si el jugador gano
                    if(didYouWin(utenteAtt.getMatrice(), row, col, '❌', utenteAtt)){
                        MessageToSend mts = new MessageToSend(message.getFrom().getId(), "Ha vinto il player! \n"
                                + "Send /start to play again!");
                        sendMessage(mts);
                    } else{
                        //Si el jugador no gano, verifico si la matriz esta full
                        if(isMatriceFull(utenteAtt.getMatrice(), utenteAtt)){
                            MessageToSend mts = new MessageToSend(message.getFrom().getId(), "Tie! :( \n"
                                    + "Send /start to play again!");
                            sendMessage(mts);
                        }
                    }
                    //Verifico si el juego sigue en curso para hacer jugar al bot
                    if(utenteAtt.getStato() == StatoGioco.INCORSO){
                        //Agarro donde jugara el bot
                        int[] posBot = playBot(utenteAtt.getMatrice());

                        //Muestro la matriz despues de la jugada del bot
                        printMatrice(message.getFrom().getId(), utenteAtt.getMatrice());

                        //Verifico si gano el bot
                        if(didYouWin(utenteAtt.getMatrice(), posBot[0], posBot[1], '⭕', utenteAtt)){
                            MessageToSend mts = new MessageToSend(message.getFrom().getId(), "Ha vinto il bot! \n" +
                                    "Send /start to play again!");
                            sendMessage(mts);
                        } else{
                            //Si no gano el bot, verifico si la matriz esta llena
                            if(isMatriceFull(utenteAtt.getMatrice(), utenteAtt)){
                                MessageToSend mts = new MessageToSend(message.getFrom().getId(), "Tie! :( \n"
                                        + "Send /start to play again!");
                                sendMessage(mts);
                            }
                        }

                    }


                }

            } else{
                MessageToSend mts = new MessageToSend(message.getFrom().getId(), "Send /start to play again!");
                sendMessage(mts);
            }
        }



    }

    @Override
    public void audioMessage(Message message) {

    }

    @Override
    public void videoMessage(Message message) {

    }

    @Override
    public void voiceMessage(Message message) {

    }

    @Override
    public void stickerMessage(Message message) {

    }

    @Override
    public void documentMessage(Message message) {

    }

    @Override
    public void photoMessage(Message message) {

    }

    @Override
    public void contactMessage(Message message) {

    }

    @Override
    public void locationMessage(Message message) {

    }

    @Override
    public void venueMessage(Message message) {

    }

    @Override
    public void newChatMemberMessage(Message message) {

    }

    @Override
    public void newChatMembersMessage(Message message) {

    }

    @Override
    public void leftChatMemberMessage(Message message) {

    }

    @Override
    public void newChatTitleMessage(Message message) {

    }

    @Override
    public void newChatPhotoMessage(Message message) {

    }

    @Override
    public void groupChatPhotoDeleteMessage(Message message) {

    }

    @Override
    public void groupChatCreatedMessage(Message message) {

    }

    @Override
    public void inLineQuery(InlineQuery inlineQuery) {

    }

    @Override
    public void chose_inline_result(ChosenInlineResult chosenInlineResult) {

    }

    @Override
    public void callback_query(CallbackQuery callbackQuery) {

    }

    @Override
    public void gameMessage(Message message) {

    }

    @Override
    public void videoNoteMessage(Message message) {

    }

    @Override
    public void pinnedMessage(Message message) {

    }

    @Override
    public void preCheckOutQueryMessage(PreCheckoutQuery preCheckoutQuery) {

    }

    @Override
    public void shippingQueryMessage(ShippingQuery shippingQuery) {

    }

    @Override
    public void invoiceMessage(Message message) {

    }

    @Override
    public void successfulPaymentMessage(Message message) {

    }

    @Override
    public void routine() {

    }
}
