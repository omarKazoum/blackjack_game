package com.omar.org;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public  class App
{
    public static  void main( String[] args )
    {
        //show the user his two randomly chosen cards as well as the opponents visible card
        //let him choose weather to stop playing and see the results or to hit if he has not lost yet
        do {
            GameManager.initGame();
            GameManager.startGame();
        }while (GameManager.promptUserForPlayAgain());

    }


}
