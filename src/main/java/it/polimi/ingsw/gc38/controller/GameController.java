package it.polimi.ingsw.gc38.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.*;

import it.polimi.ingsw.gc38.model.Card;
import it.polimi.ingsw.gc38.model.CardCornerInput;
import it.polimi.ingsw.gc38.model.Cell;
import it.polimi.ingsw.gc38.model.Coordinate;
import it.polimi.ingsw.gc38.model.Player;
import it.polimi.ingsw.gc38.model.Resource;
import it.polimi.ingsw.gc38.model.ResourceCard;
import it.polimi.ingsw.gc38.model.StarterCard;
import it.polimi.ingsw.gc38.view.CliView;

public class GameController {
    private Player player;
    private CliView view;

    private final int cardWidth;
    private final int cardHeight;
    private final int matrixDimension;

    public GameController(Player player, CliView view) {
        this.player = player;
        this.view = view;

        this.cardWidth = 7;
        this.cardHeight = 3;
        this.matrixDimension = 20;

        this.player.setMatrix(new int[matrixDimension][matrixDimension]);
        this.player.setBoard(new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth]);

        player.initializeBoard();
        player.initializeMatrix();
    }

    public void initPlayerNickname() {
        String nickname = view.askForNickname();
        player.setNickname(nickname);
    }

    public void startGame() {
        // ask for player nickname
        // initPlayerNickname();

        // welcome message
        view.welcomeMessage();

        // load starter cards
        List<Card> starterCards = loadStarterCards();
        // shuffle starter cards
        Collections.shuffle(starterCards);
        // extract one card from starter cards
        Card extractedStarterCard = (Card) starterCards.get(0);
        // visualize extracted card from starter cards
        // view.displayStarterCard(extractedStarterCard);
        // Boolean side = view.askForSide();
        // extractedStarterCard.setSide(side);

        // load resource cards
        List<Card> resourceCards = loadResourceCards();
        // shuffle resource cards
        // Collections.shuffle(resourceCards);
        // extract two cards from resource cards
        // List<Card> extractedResourceCards = new ArrayList<>();
        // extractedResourceCards.add(resourceCards.get(0));
        // extractedResourceCards.add(resourceCards.get(1));
        // visualize extracted cards from resource cards
        // for (Card card : extractedResourceCards) {
        // view.displayResourceCard((ResourceCard) card);
        // view.displayResourceCardBack((ResourceCard) card);
        // }

        player.setPlayingHand(new ArrayList<Card>());

        // add cards to the playing hand
        player.getPlayingHand().add(resourceCards.get(1));
        player.getPlayingHand().add(resourceCards.get(12));
        player.getPlayingHand().add(resourceCards.get(3));
        player.getPlayingHand().add(resourceCards.get(14));
        player.getPlayingHand().add(resourceCards.get(5));
        player.getPlayingHand().add(resourceCards.get(16));
        player.getPlayingHand().add(resourceCards.get(7));
        player.getPlayingHand().add(resourceCards.get(20));
        player.getPlayingHand().add(resourceCards.get(21));

        List<Integer> playingHandIds = new ArrayList<Integer>();
        playingHandIds.add(player.getPlayingHand().get(0).getId());
        playingHandIds.add(player.getPlayingHand().get(1).getId());
        playingHandIds.add(player.getPlayingHand().get(2).getId());
        playingHandIds.add(player.getPlayingHand().get(3).getId());
        playingHandIds.add(player.getPlayingHand().get(4).getId());
        playingHandIds.add(player.getPlayingHand().get(5).getId());
        playingHandIds.add(player.getPlayingHand().get(6).getId());
        playingHandIds.add(player.getPlayingHand().get(7).getId());
        playingHandIds.add(player.getPlayingHand().get(8).getId());

        Cell[][] matrixBoard = player.getBoard();
        List<Card> codex = new ArrayList<Card>();

        // initialize color of the board
        // view.initializeBoardColor(matrixBoard); TODO: fix this
        // for (int i = 0; i < matrixDimension*3; i++)
        // for (int j = 0; j < matrixDimension*7; j++)
        // matrixBoard[i][j].setColor(ANSI_BROWN_BACKGROUND);

        // positioning the first card in the middle of the board
        ResourceCard targetCard, cardToPlace;

        cardToPlace = (ResourceCard) resourceCards.get(0);
        // coordinates of the center of the board
        Coordinate leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5,matrixDimension / 2 * cardHeight - 5);
        cardToPlace.setSide(false);
        cardToPlace.setCentre(leftUpCorner);
        cardToPlace.setXYCord(matrixDimension / 2, matrixDimension / 2);
        player.getMatrix()[cardToPlace.getyMatrixCord()][cardToPlace.getxMatrixCord()] = cardToPlace.getId();
        view.placeCard(player.getBoard(), cardToPlace, leftUpCorner);
        codex.add(resourceCards.get(0));

        view.displayBoard(matrixBoard);

        while (true) {
            playingHandIds = new ArrayList<Integer>();
            System.out.println("Visualizing playing hand: ");
            for (Card c : player.getPlayingHand()) {
                view.displayResourceCard((ResourceCard) c);
                playingHandIds.add(c.getId());
            }

            // ask for which card to play
            CardCornerInput cardToPlay = view.askForCardToPlay(playingHandIds);

            // get card, from the resourceCards list, in the playing hand that has the id
            // selected
            Card cardToPlaySelected = player.getPlayingHand().stream()
                    .filter(card -> card.getId() == cardToPlay.getId())
                    .findFirst()
                    .orElse(null);
            cardToPlaySelected.setSide(cardToPlay.isFrontSide());

            Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();
            String out = "Seleziona la carta e l'angolo (";
            for (Card c : codex) {
                ResourceCard card = (ResourceCard) c;
                boolean pos = true;

                List<Coordinate> testCorners = new ArrayList<>();

                if (card.getActualCorners().containsKey(0) && !card.getActualCorners().get(0).isHidden()) {
                    if (player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord()] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord()])
                                .getActualCorners().containsKey(1)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 3));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord()], 1));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() - 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() - 2])
                                .getActualCorners().containsKey(3)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 1));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() - 2], 3));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord() - 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord() - 2])
                                .getActualCorners().containsKey(2)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 0));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord() - 2], 2));
                        } else
                            pos = false;
                    }

                    if (pos) {
                        Map<Integer, List<Coordinate>> testCornersMap = new HashMap<>();
                        if (!testCorners.isEmpty()) {
                            testCornersMap.put(0, testCorners);
                            test.put(card.getId(), testCornersMap);
                        }
                        out += card.getId() + ".0 / ";
                    }
                }

                pos = true;
                testCorners = new ArrayList<>();

                if (card.getActualCorners().containsKey(1) && !card.getActualCorners().get(1).isHidden()) {
                    if (player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord()] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord()])
                                .getActualCorners().containsKey(0)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 2));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord()], 0));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() - 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() - 2])
                                .getActualCorners().containsKey(2)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 0));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() - 2], 2));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord() - 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord() - 2])
                                .getActualCorners().containsKey(3)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 1));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord() - 2], 3));
                        } else
                            pos = false;
                    }

                    if (pos) {
                        Map<Integer, List<Coordinate>> testCornersMap = new HashMap<>();
                        if (!testCorners.isEmpty()) {
                            testCornersMap.put(1, testCorners);
                            test.put(card.getId(), testCornersMap);
                        }
                        out += card.getId() + ".1 / ";
                    }
                }

                pos = true;
                testCorners = new ArrayList<>();

                if (card.getActualCorners().containsKey(2) && !card.getActualCorners().get(2).isHidden()) {
                    if (player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord()] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord()])
                                .getActualCorners().containsKey(3)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 1));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord()], 3));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() + 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() + 2])
                                .getActualCorners().containsKey(1)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 3));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() + 2], 1));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord() + 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord() + 2])
                                .getActualCorners().containsKey(0)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 2));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() - 2][card.getxMatrixCord() + 2], 0));
                        } else
                            pos = false;
                    }

                    if (pos) {
                        Map<Integer, List<Coordinate>> testCornersMap = new HashMap<>();
                        if (!testCorners.isEmpty()) {
                            testCornersMap.put(2, testCorners);
                            test.put(card.getId(), testCornersMap);
                        }
                        out += card.getId() + ".2 / ";
                    }
                }

                pos = true;
                testCorners = new ArrayList<>();

                if (card.getActualCorners().containsKey(3) && !card.getActualCorners().get(3).isHidden()) {
                    if (player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord()] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord()])
                                .getActualCorners().containsKey(2)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 0));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord()], 2));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() + 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() + 2])
                                .getActualCorners().containsKey(0)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 2));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord() + 2], 0));
                        } else
                            pos = false;
                    }
                    if (player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord() + 2] != -1) {
                        if (resourceCards.get(player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord() + 2])
                                .getActualCorners().containsKey(1)) {
                            testCorners.add(new Coordinate(cardToPlaySelected.getId(), 3));
                            testCorners.add(new Coordinate(
                                    player.getMatrix()[card.getyMatrixCord() + 2][card.getxMatrixCord() + 2], 1));
                        } else
                            pos = false;
                    }

                    if (pos) {
                        Map<Integer, List<Coordinate>> testCornersMap = new HashMap<>();
                        if (!testCorners.isEmpty()) {
                            testCornersMap.put(3, testCorners);
                            test.put(card.getId(), testCornersMap);
                        }
                        out += card.getId() + ".3 / ";
                    }
                }
            }

            out = out.substring(0, out.length() - 2);
            out += "): ";

            // ask for which card attach the selected card
            String cardToAttachSelected = view.askForCardToAttach(out);
            String[] splitCardToPlay = cardToAttachSelected.split("\\.");
            int cornerSelected = Integer.parseInt(splitCardToPlay[1]);
            targetCard = (ResourceCard) resourceCards.get(Integer.parseInt(splitCardToPlay[0]));

            if (test.containsKey(Integer.parseInt(splitCardToPlay[0]))) {
                if (test.get(Integer.parseInt(splitCardToPlay[0])).containsKey(cornerSelected)) {
                    List<Coordinate> co = test.get(Integer.parseInt(splitCardToPlay[0])).get(cornerSelected);

                    for (Coordinate c : co) {
                        if (c.getX() == cardToPlaySelected.getId()
                                && cardToPlaySelected.getActualCorners().containsKey(c.getY())) {
                            cardToPlaySelected.getActualCorners().get(c.getY()).setHidden(true);
                        } else if (resourceCards.get(c.getX()).getActualCorners().containsKey(c.getY())) {
                            resourceCards.get(c.getX()).getActualCorners().get(c.getY()).setHidden(true);
                            resourceCards.get(c.getX()).getActualCorners().get(c.getY()).setEmpty(true);
                        }
                    }
                }
            }

            codex.add(cardToPlaySelected);
            player.getPlayingHand().remove(cardToPlaySelected);

            if (cornerSelected == 0) {
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlaySelected,
                        placeCardBottomLeft(player.getBoard(), targetCard, (ResourceCard) cardToPlaySelected));
                cardToPlaySelected.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 1) {
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlaySelected,
                        placeCardTopLeft(player.getBoard(), targetCard, (ResourceCard) cardToPlaySelected));
                cardToPlaySelected.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 2) {
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlaySelected,
                        placeCardTopRight(player.getBoard(), targetCard, (ResourceCard) cardToPlaySelected));
                cardToPlaySelected.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() + 1);
            } else if (cornerSelected == 3) {
                cardToPlaySelected.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() + 1);
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlaySelected,
                        placeCardBottomRight(player.getBoard(), targetCard, (ResourceCard) cardToPlaySelected));
            }

            player.getMatrix()[cardToPlaySelected.getyMatrixCord()][cardToPlaySelected
                    .getxMatrixCord()] = cardToPlaySelected.getId();

            // counting the number of resources on the codex
            int animal = 0;
            int fungi = 0;
            int plant = 0;
            int insect = 0;
            int quill = 0;
            int noun = 0;
            int manuscript = 0;

            for (Card c : codex) {
                // iterate on every corner of the card
                if (!c.isFrontSide()) {
                    if (c.getBackResources().get(0) == Resource.ANIMAL)
                        animal += 1;
                } else {
                    for (int i = 0; i < 4; i++) {
                        if (c.getFrontCorners().containsKey(i) && !c.getFrontCorners().get(i).isEmpty()) {
                            if (c.getFrontCorners().get(i).getResource() == Resource.ANIMAL)
                                animal += 1;
                            if (c.getFrontCorners().get(i).getResource() == Resource.FUNGI)
                                fungi += 1;
                            if (c.getFrontCorners().get(i).getResource() == Resource.PLANT)
                                plant += 1;
                            if (c.getFrontCorners().get(i).getResource() == Resource.INSECT)
                                insect += 1;
                            if (c.getFrontCorners().get(i).getResource() == Resource.QUILL)
                                quill += 1;
                            if (c.getFrontCorners().get(i).getResource() == Resource.NOUN)
                                noun += 1;
                            if (c.getFrontCorners().get(i).getResource() == Resource.MANUSCRIPT)
                                manuscript += 1;
                        }
                    }
                }
            }

            System.out.println();

            view.displayBoard(matrixBoard);

            System.out.println("Visualizing codex resources: ");
            System.out.println();
            System.out.println("Animal: " + animal);
            System.out.println("Fungi: " + fungi);
            System.out.println("Plant: " + plant);
            System.out.println("Insect: " + insect);
            System.out.println("Quill: " + quill);
            System.out.println("Noun: " + noun);
            System.out.println("Manuscript: " + manuscript);
            System.out.println();
        }
    }

    public Coordinate placeCardBottomRight(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        card.getActualCorners().get(3).setHidden(true);
        card.getActualCorners().get(3).setEmpty(true);
        if (cardToPlace.getActualCorners().containsKey(1)) {
            cardToPlace.getActualCorners().get(1).setHidden(true);
        }

        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopRight(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        card.getActualCorners().get(2).setHidden(true);
        card.getActualCorners().get(2).setEmpty(true);
        if (cardToPlace.getActualCorners().containsKey(0)) {
            cardToPlace.getActualCorners().get(0).setHidden(true);
        }

        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardBottomLeft(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        card.getActualCorners().get(0).setHidden(true);
        card.getActualCorners().get(0).setEmpty(true);
        if (cardToPlace.getActualCorners().containsKey(2))
            cardToPlace.getActualCorners().get(2).setHidden(true);

        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopLeft(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        card.getActualCorners().get(1).setHidden(true);
        card.getActualCorners().get(1).setEmpty(true);
        if (cardToPlace.getActualCorners().containsKey(3))
            cardToPlace.getActualCorners().get(3).setHidden(true);

        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public List<Card> loadStarterCards() {
        List<Card> cards = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<StarterCard>>() {
            }.getType();
            cards = gson.fromJson(new FileReader("src\\main\\resources\\it\\polimi\\ingsw\\gc38\\starterCards.json"),
                    listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return cards;
    }

    public List<Card> loadResourceCards() {
        List<Card> cards = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ResourceCard>>() {
            }.getType();
            cards = gson.fromJson(new FileReader("src\\main\\resources\\it\\polimi\\ingsw\\gc38\\resourceCards.json"),
                    listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return cards;
    }
}
