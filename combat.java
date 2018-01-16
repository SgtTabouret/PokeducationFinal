import extensions.CSVFile;
class combat extends Program{
//RAJOUTER FONCTION TEST POUR VERIFIER QUE LES REPONSES PROPOSES CONTIENNENT UNE BONNE
String[] attaquesDispo = new String[] {"Charge (Histoire)","Morsure (Maths)","Griffe (Divers)","Tranche (Francais)"};
String cheminCSV = "C:\\Users\\Flann\\Documents\\NetBeansProjects\\Pokeducation\\src\\";
double facteurLv = 1;

    String[][] csvToTab (CSVFile fichier){

    int nbCol = columnCount(fichier);
    int nbLigne = rowCount(fichier);

    String[][] tableau= new String [nbLigne][nbCol];
    for (int i = 0 ; i< length(tableau,1); i++){ //compte les Lignes
        for (int j = 0 ; j < length(tableau,2); j++){ //les Colonnes
        tableau[i][j] = getCell(fichier,i,j);
      }
    }
    return tableau;
    }

    String[][] conversionCsvQtoTabQ(String Attaque){
        String[][]tabQ=new String [50][50];
              if (Attaque == "Charge (Histoire)"){
                  CSVFile QHistoire = loadCSV(cheminCSV + "QHistoire.csv");
                  tabQ=csvToTab(QHistoire);
              }else if (Attaque == "Morsure (Maths)"){
                 // CSVFile QMaths = loadCSV(cheminCSV + "QHMaths.csv");
               //   tabQ=csvToTab(QMaths);
              }else if (Attaque == "Griffe (Divers"){
                 // CSVFile QDivers = loadCSV(cheminCSV + "QDivers.csv");
                 // tabQ=csvToTab(QDivers);
              }else if (Attaque == "Tranche (Francais)"){
                  //CSVFile QFrancais = loadCSV(cheminCSV + "QFrancais.csv");
                  //tabQ=csvToTab(QFrancais);
              }
        return tabQ;
    }


  void testChoixAttaque () {
    assertEquals("Charge",choixAttaque(1,attaquesDispo));
    assertEquals("Griffe",choixAttaque(3,attaquesDispo));
  }
  String choixAttaque(int choix,String[] attaquesDispo) {
    //demqnde au joueur de choisir un attaque en controlant la saisie
    choix = choix - 1;
    String attaqueChoisie = attaquesDispo[choix];

    //CONTROLE SAISIE A AJOUTER

    return attaqueChoisie;
  }

  int calculDegats(String Attaque, int PATK, String pokeJoueur, String pokeEnnemi){
    //calcul les degats infliges en fonction de la reussite de l'attaque
      int degats;
      boolean reussite;
      reussite = reussiteAttaque (Attaque);
      degats = assignationDegats(reussite, PATK, pokeJoueur, pokeEnnemi);
      return degats;
  }

  boolean reussiteAttaque(String Attaque){
      //calcul la reussite de lattaque en fonction de la bonne ou mauvaise reponse du joueur
      boolean reussite = false;
      String[][] tabQ =conversionCsvQtoTabQ(Attaque);
      int nQuestion = (int) (random()*length(tabQ,1));
      println(tabQ[nQuestion][0] + "\n");
      affichageRep(tabQ, nQuestion);
      int nRepChoisie= readInt();
      if (tabQ[nQuestion][nRepChoisie].equals(tabQ[nQuestion][5])){
          reussite=true;
      }
      return reussite;
  }
  void testAssignationDegats(){
      assertEquals(4, assignationDegats(true, 3, "poke1", "poke2"));
      assertEquals(1,assignationDegats(false, 2, "poke1", "poke2"));
  }
  int assignationDegats(boolean reussite,int PATK, String pokeJoueur, String pokeEnnemi){
     int degats;
     if (reussite == true){
         degats = PATK+1;
         println("Bien joué ! " + pokeJoueur + " a frappé " + pokeEnnemi + " de plein fouet ! ");
     } else {
         degats = (int) (PATK*0.5);
         println("Mince ! " + pokeJoueur + " a raté son attaque !");
     }
     return degats;
  }
  
  void affichageRep(String[][] tabQ, int nQuestion){
          for(int i = 1; i<5;i++){
            println(i + "." + tabQ[nQuestion][i]);
          }
  }

  Pokemon apparitionPokemon (String[][] tableau, double facteurLvl){

      int randomNb = 0;
    while(randomNb == 0){
        randomNb=(int) (random()*length(tableau,2));
    }
    Pokemon pokemonApparaissant = new Pokemon();
    pokemonApparaissant.Nom      = tableau [0][randomNb];
    pokemonApparaissant.PV       = (int) (5 * facteurLvl) ;
    pokemonApparaissant.PATK     = (int) (1 * facteurLvl) ;
    pokemonApparaissant.TypePoke = tableau [1][randomNb];
    return pokemonApparaissant;
    }


  void phraseAttaque(int i, String nomPokeJ, String nomPokeA){
      if (i == 0){
          println("Quelle attaque " + nomPokeJ + " doit-il lancer ? : ");
      } else {
          println(nomPokeA + " est encore debout !");
      }
  }


  void algorithm(){
    //definition variables
    CSVFile pokemons = loadCSV(cheminCSV + "pokemons.csv"); //MODIFIER PLUS TARD
    String[][]  lesPokemons =  csvToTab(pokemons);
 

    Pokemon pokemonJoueur = new Pokemon();
    pokemonJoueur.Nom      = lesPokemons [0][0];
    pokemonJoueur.PV       = 10;
    pokemonJoueur.PATK     = 2;
    pokemonJoueur.TypePoke = lesPokemons [1][0];
    int idxTour=0;

    //apparition pokemon random
    Pokemon pokemonApparu = apparitionPokemon(lesPokemons, facteurLv);
    clearScreen();
    println("Un " + pokemonApparu.Nom + " est apparu !");


    //debut combat
    while (pokemonApparu.PV > 0 && pokemonJoueur.PV > 0){
        int degats;
        phraseAttaque(idxTour, pokemonJoueur.Nom, pokemonApparu.Nom);

        //attaque de l'utilisateur


        String attaqueLancee = choixAttaque(readInt(),attaquesDispo);
        clearScreen();
        cursor(16,4);
        println(pokemonJoueur.Nom + " attaque " + attaqueLancee + "!");
        delay(1000);
        clearScreen();
        degats = calculDegats(attaqueLancee, pokemonJoueur.PATK, pokemonJoueur.Nom, pokemonApparu.Nom);

        // perte de PVs
        pokemonApparu.PV = (int) (pokemonApparu.PV-degats);
        
        //riposte de l'ennemi
        //fin combat quand l'ennemi ou le joueur n'a plus de PV
        //gain d'exp
        idxTour=idxTour+1;
    }
    facteurLv = facteurLv + 0.2;
  }
}
