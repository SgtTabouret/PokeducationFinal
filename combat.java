import extensions.CSVFile;
class combat extends Program{
//RAJOUTER FONCTION TEST POUR VERIFIER QUE LES REPONSES PROPOSES CONTIENNENT UNE BONNE
String[] attaquesDispo = new String[] {"Charge (Histoire)","Morsure (Maths)","Griffe (Culture)","Tranche (Francais)"};
String cheminCSV = "C:\\Users\\Flann\\Desktop\\Pokeducation\\src\\pokeducation\\PokeducationFinal\\";
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
    
    String[][] conversionCsvQtoTabQ(String typeAttaque){
        String nomCSV = "Q"+typeAttaque+".csv";
        CSVFile questions = loadCSV(cheminCSV + nomCSV);
        String[][] tabQ=csvToTab(questions);
        return tabQ;
    }
    void testRecupererTypeAttaque(){
        assertEquals("Francais", recupererTypeAttaque("Tranche (Francais)"));
        assertEquals("Toto", recupererTypeAttaque("TuTu (Toto)"));
    }
    String recupererTypeAttaque(String attaque) {
        String typeAttaque="";
        int i=0;
        while (!")".equals(substring(attaque,i,i+1))){
            i=i+1;
            if ("(".equals(substring(attaque,i,i+1))){
                i=i+1;
                while (!")".equals(substring(attaque,i,i+1))){
                    typeAttaque=typeAttaque+substring(attaque,i,i+1);
                    i=i+1;
                }
            }
        }
        return typeAttaque;
    }
    
  void testChoixAttaque () {
    assertEquals("Charge (Histoire)",choixAttaque("1",attaquesDispo));
    assertEquals("Griffe (Culture)",choixAttaque("3",attaquesDispo));
  }
  String choixAttaque(String choix,String[] attaquesDispo) {
    //demqnde au joueur de choisir un attaque en controlant la saisie
    while(!choix.equals("1") && !choix.equals("2") && !choix.equals("3") && !choix.equals("4")){
    println("Mettre un chiffre compris entre 1 et 4");
      delay(2000);
      choix = readString();
    }
    int choixInt=stringToInt(choix);
    choixInt = choixInt - 1;
    String attaqueChoisie = attaquesDispo[choixInt];

    //CONTROLE SAISIE A AJOUTER

    return attaqueChoisie;
  }

  int calculDegats(String attaque, Pokemon pokeEnnemi, Pokemon pokeJoueur){
    //calcul les degats infliges en fonction de la reussite de l'attaque
      int degats;
      boolean reussite;
      reussite = reussiteAttaque (attaque);
      degats = assignationDegats(reussite, pokeJoueur.PATK, pokeJoueur.Nom, pokeEnnemi.Nom, pokeJoueur.TypePoke);
      return degats;
  }

  boolean reussiteAttaque(String attaque){
      //calcul la reussite de lattaque en fonction de la bonne ou mauvaise reponse du joueur
      boolean reussite = false;
      String typeAttaque=recupererTypeAttaque(attaque);
      String[][] tabQ =conversionCsvQtoTabQ(typeAttaque);
      int nQuestion = (int) (random()*length(tabQ,1));
      println(tabQ[nQuestion][0] + "\n");
      affichageRep(tabQ, nQuestion);
      String nRepChoisieString= readString();
      while(!nRepChoisieString.equals("1") && !nRepChoisieString.equals("2") && !nRepChoisieString.equals("3") && !nRepChoisieString.equals("4")){
    println("Mettre un chiffre compris entre 1 et 4");
      delay(2000);
      nRepChoisieString = readString();
    }
    int nRepChoisie=stringToInt(nRepChoisieString);
      if (tabQ[nQuestion][nRepChoisie].equals(tabQ[nQuestion][5])){
          reussite=true;
      }
      return reussite;
  }
  void testAssignationDegats(){
      assertEquals(4, assignationDegats(true, 3, "poke1", "poke2", "histoire"));
      assertEquals(1,assignationDegats(false, 2, "poke1", "poke2", "histoire"));
  }
  int assignationDegats(boolean reussite,int PATK, String pokeJoueur, String pokeEnnemi, String typePokeJoueur){
     int degats;
     if (reussite == true){
         degats = PATK+1;
         println("Bien joué ! " + pokeJoueur + " a frappé " + pokeEnnemi + " de plein fouet ! ");
     } else {
         degats = (int) (PATK*0.5);
         println("Mince ! " + pokeJoueur + " n'a pas lancé sa meilleure attaque !");
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
 

    Pokemon pokemonJoueur = new Pokemon(); //a mettre dans le fichier sauvegarde
    pokemonJoueur.Nom      = lesPokemons [0][0];
    pokemonJoueur.PV       = 10;
    pokemonJoueur.PATK     = 2;
    pokemonJoueur.TypePoke = lesPokemons [1][0];
    // pokemonJoueur.niveau = niv Sauvegarde ds le fichier csv
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


        String attaqueLancee = choixAttaque(readString(),attaquesDispo);
        clearScreen();
        cursor(16,4);
        println(pokemonJoueur.Nom + " attaque " + attaqueLancee + "!");
        delay(800);
        clearScreen();
        degats = calculDegats(attaqueLancee, pokemonApparu, pokemonJoueur);

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
