package com.omar.org;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager {
    private static List<Card> playerCardsList;
    private static List<Card> bankCardsList;
    private static List<Card> piocheCardsList;

    public static void initGame(){
        System.out.println("--------Welcome To Blackjack-------");
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
            options.put(Constants.PLAYER_ACTION_STAND,"stand");
            options.put(Constants.PLAYER_ACTION_HIT,"hit");
            int option=-1;
            while (!options.containsKey(option)) {
                System.out.println("Que souhaitez vous faire?");
                options.keySet().forEach(ok -> System.out.println(ok + "=>" + options.get(ok)));
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
        System.out.println("your cards:");
        printCardsValues(playerCardsList);
        System.out.println("value:"+getCardsSum(playerCardsList));
        System.out.println("dealer's "+(showAll?"":"visible")+" cards:");
        printCardsValues(bankCardsList.stream().skip(showAll?0:1).collect(Collectors.toList()));
        System.out.println("value:"+getCardsSum(bankCardsList.stream().skip(1).collect(Collectors.toList())));
    }

    private static void playerActionStand() {
        showResults();
    }

    private static void showResults() {
        showCards(true);
        //player cards alone are beyond 21
        //dealer cards alone are beyond 21
        //player and dealer are beyond 21
        //dealer or player has 21
        //equal values
        //one has point more than the other

        //bust
        //win
        //lost

        int playerPoints=getCardsSum(playerCardsList);
        int dealerPoints=getCardsSum(bankCardsList);
        if(playerPoints>21){
            System.out.println("Perdu!");
        }else if(playerPoints==21){
            System.out.println("Blackjack!");
        }else if(playerPoints>dealerPoints){
            System.out.println("Gagné!");
        }else{
            System.out.println("Perdu!");
        }
        /*
        if(playerPoints==dealerPoints){
            System.out.println("Vous avez eu une équivalence");
            return;
        }else if(dealerPoints==21 || (playerPoints>21 && dealerPoints<21)){
            System.out.println("La banque gagne");
        }else if(playerPoints==21 || (playerPoints<21 && playerPoints>dealerPoints)){
            System.out.println("Vous avez gagné");
        }
*/






    }

    private static void playActionHit() {
        playerCardsList.add(pullRandomCard());
    }

    public static boolean isLost(){
        return getCardsSum(playerCardsList)>21;
    }

    public static boolean promptUserForPlayAgain(){
        System.out.println("Voulez vous jouer un autre tourne (tapez 'O' pour Oui ou bien un autre caractère pour quiter)?");
        return getScanner().next().equalsIgnoreCase("o");

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

    //double only for the first turn (for the player)
    //second time > hit(take another card) or stand
    //





}
