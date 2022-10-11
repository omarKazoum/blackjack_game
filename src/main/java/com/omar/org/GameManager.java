package com.omar.org;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager {
    private static List<Card> playerCardsList;
    private static List<Card> bankCardsList;
    private static List<Card> piocheCardsList;

    public static void initGame(){
        changeConsoleColorTo(ConsoleColors.YELLOW_BOLD);
        System.out.print("\uD83D\uDD90\uD83D\uDD90\uD83D\uDD90\uD83D\uDD90\uD83D\uDD90");
        changeConsoleColorTo(ConsoleColors.RESET);
        System.out.print("Welcome To Blackjack");
        changeConsoleColorTo(ConsoleColors.YELLOW_BOLD);
        System.out.println("\uD83D\uDD90\uD83D\uDD90\uD83D\uDD90\uD83D\uDD90\uD83D\uDD90");
        changeConsoleColorTo(ConsoleColors.RESET);
        System.out.println("");
        clearScreen();
        playerCardsList=new LinkedList<>();
        bankCardsList=new LinkedList<>();
        piocheCardsList=getOrderedCardsLIst();
    }
    public static void startGame(){
        //pull two cards for the player
        playerCardsList.add(pullRandomCard());
        playerCardsList.add(pullRandomCard());
        //pull two cards for the dealer
        bankCardsList.add(pullRandomCard());
        bankCardsList.add(pullRandomCard());
        boolean isStand=false;
        while(!(isBlackJack() || isLost() || isStand || piocheCardsList.isEmpty()) ){
            showCards(false);
            Map<Integer,String> options=new Hashtable<>();
            options.put(Constants.PLAYER_ACTION_HIT,ConsoleColors.PURPLE_BOLD+"%s"+"\uD83D\uDC50"+"Piocher"+ConsoleColors.RESET);
            options.put(Constants.PLAYER_ACTION_STAND,ConsoleColors.YELLOW_BOLD+"%s"+"\uD83E\uDD1A"+"Arréter"+ConsoleColors.RESET);
            int option=-1;
            while (!options.containsKey(option)) {
                System.out.println("Que souhaitez vous faire?");
                options.keySet().forEach(ok -> System.out.println(String.format(options.get(ok),ok + "=>") ));
                option=getScanner().nextInt();
            }
            switch (option){
                case Constants.PLAYER_ACTION_HIT:
                    playActionHit();
                    break;
                case Constants.PLAYER_ACTION_STAND:
                    isStand=true;
                    break;
            }
        }
        showResults();
    }
    private static void showCards(boolean showAll) {
        changeConsoleColorTo(ConsoleColors.BLUE);
        System.out.println("Vos cartes:");
        printCardsValues(playerCardsList);
        System.out.println("value:"+getCardsSum(playerCardsList));
        changeConsoleColorTo(ConsoleColors.YELLOW);
        System.out.println("dealer's "+(showAll?"":"visible")+" cards:");
        printCardsValues(bankCardsList.stream().skip(showAll?0:1).collect(Collectors.toList()));
        System.out.println("value:"+getCardsSum(bankCardsList.stream().skip(showAll?0:1).collect(Collectors.toList())));
        changeConsoleColorTo(ConsoleColors.RESET);
    }
    private static void showResults() {
        showCards(true);
        int playerPoints=getCardsSum(playerCardsList);
        int dealerPoints=getCardsSum(bankCardsList);
        int result=0;
        if(playerPoints==21){
            result=Constants.GAME_OUTCOME_BLACKJACK;
        }else if(playerPoints>21){
            result=Constants.GAME_OUTCOME_LOST;
        }else if(playerPoints<21){
            if(dealerPoints==21)
                result=Constants.GAME_OUTCOME_LOST;
            else if(dealerPoints<21){
                if(playerPoints>dealerPoints){
                    result=Constants.GAME_OUTCOME_WON;
                }else if(playerPoints<dealerPoints)
                    result=Constants.GAME_OUTCOME_LOST;
                else
                    result=Constants.GAME_OUTCOME_SETTLE;;
            }else
                result=Constants.GAME_OUTCOME_WON;;
        }else{
            result=Constants.GAME_OUTCOME_LOST;;
        }
        printFinaleMessage(result);
    }
    private static void playActionHit() {
        playerCardsList.add(pullRandomCard());
    }
    public static boolean isLost(){
        return getCardsSum(playerCardsList)>21;
    }
    public static boolean promptUserForPlayAgain(){
        System.out.println("Voulez vous jouer un autre tourne (tapez 'O' pour Oui ou bien un autre caractère pour quiter)?");
        return getScanner().next().equalsIgnoreCase("O");

    }
    public static Card pullRandomCard(){
        int randomIndex=new Random().nextInt(piocheCardsList.size());
        return piocheCardsList.remove(randomIndex);
    }
    public static List<Card> getOrderedCardsLIst(){
        List<Card> cards=Stream.of(Card.Height.values()).map(cv->
            Stream.of(Card.Color.values()).map(cc->
                 new Card(cv,cc)
            ).collect(Collectors.toList())

        ).reduce(new ArrayList<>(),(l,c)->{l.addAll(c);return l;});
        return cards;
    }
    public static void printCardsValues(List<Card> cards){
        System.out.println(cards.stream().map(c->String.format("%s de %s",c.cardHeight.getIntValue(),c.cardColor.toString().toLowerCase())).collect(Collectors.joining("\n")));
    }
    public static Scanner getScanner(){
        return new Scanner(System.in);
    }
    public static void clearScreen(){

    }
    public static int getCardsSum(List<Card> cards){
        int cardsTotalValue=0;
        cardsTotalValue+=cards.stream().filter(c->c.cardHeight!=Card.Height.AS_1).map(c->c.cardHeight.getIntValue()).map(iv->iv<=10?iv:10).reduce(0,Integer::sum);
        long asCardsCount=cards.stream().filter(c->c.cardHeight==Card.Height.AS_1).count();
        if(asCardsCount==1){
                if(cardsTotalValue+11>21)
                    cardsTotalValue+=1;
                else
                    cardsTotalValue+=11;
        }else if(asCardsCount>1){
            cardsTotalValue+=asCardsCount;
        }

        return cardsTotalValue;
    }
    public static boolean isBlackJack(){
        return getCardsSum(playerCardsList)==21;

    }
    public static void changeConsoleColorTo(String color){
        System.out.print(color);
    }
    public static void printFinaleMessage(int result){

        switch(result){
            case Constants.GAME_OUTCOME_LOST:
                changeConsoleColorTo(ConsoleColors.RED_BOLD_BRIGHT);
                System.out.println("\uD83D\uDE35Perdu\uD83D\uDE35");
                break;
            case Constants.GAME_OUTCOME_BLACKJACK:
                changeConsoleColorTo(ConsoleColors.GREEN_BOLD_BRIGHT);
                System.out.println("\uD83E\uDD29Blackjack!\uD83E\uDD29");
                break;
            case Constants.GAME_OUTCOME_WON:
                changeConsoleColorTo(ConsoleColors.GREEN_BOLD);
                System.out.println("\uD83D\uDE00gagné\uD83D\uDE00");
                break;
            case Constants.GAME_OUTCOME_SETTLE:
                changeConsoleColorTo(ConsoleColors.YELLOW_BOLD);
                System.out.println("\uD83D\uDE4Fégalité\uD83D\uDE4F");
                break;
        }
        changeConsoleColorTo(ConsoleColors.RESET);
    }





}
