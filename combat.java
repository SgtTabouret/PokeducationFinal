import extensions.CSVFile;
class combat extends Program{
//RAJOUTER FONCTION TEST POUR VERIFIER QUE LES REPONSES PROPOSES CONTIENNENT UNE BONNE
String cheminCSV = "C:\\Users\\jules\\Desktop\\Pokeducation\\src\\pokeducation\\PokeducationFinal\\";


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
    String recupererTypeAttaque(String Attaque) {
        String typeAttaque="";
        int i=0;
        while (!")".equals(substring(Attaque,i,i+1))){
            i=i+1;
            if ("(".equals(substring(Attaque,i,i+1))){
                i=i+1;
                while (!")".equals(substring(Attaque,i,i+1))){
                    typeAttaque=typeAttaque+substring(Attaque,i,i+1);
                    i=i+1;
                }
            }
        }
        return typeAttaque;
    }
    
  void testChoixAttaque () {
      String[] attaques = new String[] {"Charge (Histoire)","Morsure (Maths)","Griffe (Culture)","Tranche (Francais)"};

    assertEquals("Charge (Histoire)",choixAttaque("1",attaques));
    assertEquals("Griffe (Culture)",choixAttaque("3",attaques));
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

  int calculDegats(String Attaque, Pokemon pokeEnnemi, Pokemon pokeJoueur){
    //calcul les degats infliges en fonction de la reussite de l'attaque
      int degats;
      boolean reussite;
      reussite = reussiteAttaque (Attaque);
      degats = assignationDegats(reussite, pokeJoueur.PATK, pokeJoueur.Nom, pokeEnnemi.Nom, pokeJoueur.TypePoke);
      return degats;
  }

  boolean reussiteAttaque(String Attaque){
      //calcul la reussite de lattaque en fonction de la bonne ou mauvaise reponse du joueur
      boolean reussite = false;
      String typeAttaque=recupererTypeAttaque(Attaque);
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
         println("Bien joue ! " + pokeJoueur + " a frappe " + pokeEnnemi + " de plein fouet ! ");
     } else {
         degats = (int) (PATK*0.5);
         println("Mince ! " + pokeJoueur + " n'a pas lance sa meilleure attaque !");
     }
     return degats;
  }
  
  void affichageRep(String[][] tabQ, int nQuestion){
          for(int i = 1; i<5;i++){
            println(i + "." + tabQ[nQuestion][i]);
          }
  }

  Pokemon apparitionPokemon (String[][] tableau, int niveau){

      int randomNb = 0;
    while(randomNb <3){
        randomNb=(int) (random()*length(tableau,2));
    }
    Pokemon pokemonApparaissant = new Pokemon();
    pokemonApparaissant.Nom      = tableau [0][randomNb];
    pokemonApparaissant.PV       = (int) (5 + niveau*3) ;
    pokemonApparaissant.PATK     = (int) (2 + niveau) ;
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


  void DebutDeJeu(){
    //definition variables
    CSVFile pokemons = loadCSV(cheminCSV + "pokemons.csv"); //MODIFIER PLUS TARD
    String[][]  lesPokemons =  csvToTab(pokemons);
    CSVFile sauvegarde = loadCSV(cheminCSV + "Sauvegarde.csv");
    String[][] laSauvegarde = csvToTab(sauvegarde);
    int sauvegardeChoisie=1;
    int idxTour=stringToInt(laSauvegarde[sauvegardeChoisie][1]);
    int xp=stringToInt(laSauvegarde[sauvegardeChoisie][11]);
    int palierxp= stringToInt(laSauvegarde[sauvegardeChoisie][12]);
    boolean findecombat=false;
    boolean joueurGagne=true;
    int SauvegardePVJoueur;
    Pokemon pokemonJoueur = new Pokemon();
    if (idxTour==0){ //permettra au joueur de commencer avec le pokemon de son choix si il s'agit de son premier tour
        println("Choisi ton pokemon");
        for (int cpt = 0 ; cpt< 3; cpt++){ 
            println(cpt+1 + ". " + lesPokemons[0][cpt] + " " + "(" + lesPokemons[1][cpt]+")");
        }
        String choixPoke=readString();
        while(!choixPoke.equals("1") && !choixPoke.equals("2") && !choixPoke.equals("3")){
            println("Mettre un chiffre compris entre 1 et 3");
            delay(2000);
            choixPoke = readString();
        }
        int choixIntPoke=stringToInt(choixPoke)-1;
        pokemonJoueur.Nom      = lesPokemons [0][choixIntPoke];
        pokemonJoueur.PV       = 10;
        pokemonJoueur.PATK     = 2;
        pokemonJoueur.TypePoke = lesPokemons [1][choixIntPoke];
        pokemonJoueur.niveau = 1;
    }
    else{
        pokemonJoueur.Nom      = laSauvegarde [sauvegardeChoisie][2];
        pokemonJoueur.PV       = stringToInt(laSauvegarde [sauvegardeChoisie][3]);
        pokemonJoueur.PATK     = stringToInt(laSauvegarde [sauvegardeChoisie][4]);
        pokemonJoueur.TypePoke = laSauvegarde [sauvegardeChoisie][5];
        pokemonJoueur.niveau = stringToInt(laSauvegarde[sauvegardeChoisie][6]);
        xp=stringToInt(laSauvegarde[sauvegardeChoisie][11]);
    
    }
    SauvegardePVJoueur=pokemonJoueur.PV;
    while(findecombat==false){
        String[] attaquesDispo = new String[] {laSauvegarde[sauvegardeChoisie][7],laSauvegarde[sauvegardeChoisie][8],laSauvegarde[sauvegardeChoisie][9],laSauvegarde[sauvegardeChoisie][10]};
    //apparition pokemon random
        Pokemon pokemonApparu = apparitionPokemon(lesPokemons, pokemonJoueur.niveau);
        clearScreen();
        println("Un " + pokemonApparu.Nom + " est apparu !");


    //debut combat
        while (pokemonApparu.PV > 0 && pokemonJoueur.PV > 0){
            int degats;
            phraseAttaque(idxTour, pokemonJoueur.Nom, pokemonApparu.Nom);

        //attaque de l'utilisateur

            println("1. "+ attaquesDispo[0] + "\n" + "2. " + attaquesDispo[1] + "\n"+ "3. " +attaquesDispo[2] + "\n" + "4. " +attaquesDispo[3]);
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
                if(pokemonApparu.PV > 0 && random()<0.9 ){
                    pokemonJoueur.PV=pokemonJoueur.PV-pokemonApparu.PATK;
                    println(pokemonApparu.Nom + " a reussi son attaque");
                    println("Il reste "+pokemonJoueur.PV+" PV a ton pokemon");
                }
                else{
                    println(pokemonApparu.Nom +" a loupe son attaque");
                }
                if(pokemonJoueur.PV<=0){
                    joueurGagne=false;
                }
        //fin combat quand l'ennemi ou le joueur n'a plus de PV
        //gain d'exp
               
            }
            pokemonJoueur.PV=SauvegardePVJoueur;
            if(joueurGagne==true){
                println("Bravo tu as vaincu "+pokemonApparu.Nom);
                xp = xp + 200;
                if(xp==palierxp){//opti possible
                    pokemonJoueur.niveau=pokemonJoueur.niveau+1;
                    println("Bravo ton pokemon est monte d'un niveau");
                    pokemonJoueur.PV=pokemonJoueur.PV+2;
                    pokemonJoueur.PATK=(int)(pokemonJoueur.PATK+ pokemonJoueur.niveau*1.5);
                    palierxp=palierxp+50;
                    if(pokemonJoueur.niveau==5){
                        println("Ton pokemon peut apprendre l'attaque Pistolet(Technologie) \n veut tu lui apprendre?\n oui \n non");
                        String nouvelleattaque=readString();
                        while(!nouvelleattaque.equals("oui") && !nouvelleattaque.equals("non")){
                            println("Mettre oui ou non");
                            delay(2000);
                            nouvelleattaque= readString();
                        }
                        if(nouvelleattaque.equals("oui")){
                            println("Que voulez vous remplacez? \n 1. "+ attaquesDispo[0] + "\n" + "2. " + attaquesDispo[1] + "\n"+ "3. " +attaquesDispo[2] + "\n" + "4. " +attaquesDispo[3]);
                            String attaquechoisi=readString();
                            while(!attaquechoisi.equals("1") && !attaquechoisi.equals("2") && !attaquechoisi.equals("3") && !attaquechoisi.equals("4")){
                                println("Mettre un chiffre compris entre 1 et 4");
                                delay(2000);
                                attaquechoisi = readString();
                            }
                            int choixattaque=stringToInt(attaquechoisi);
                            choixattaque = choixattaque - 1;
                            attaquesDispo[choixattaque]="Pistolet (Technologie";
                        }
                    }
                }
                idxTour=idxTour+1;
            }
            else{
                println("Bien tentez, retente ta chance");
            }
            joueurGagne=true;
            println("Voulez vous continuez? \n oui \n non");
            String continuer=readString();
            while(!continuer.equals("oui") && !continuer.equals("non")){
            println("Mettre oui ou non");
            delay(2000);
            continuer= readString();
            }
            if(continuer.equals("non")){
                findecombat=true;
                laSauvegarde[sauvegardeChoisie][1]=""+idxTour;
                laSauvegarde[sauvegardeChoisie][2]=pokemonJoueur.Nom;
                laSauvegarde[sauvegardeChoisie][3]=""+pokemonJoueur.PV;
                laSauvegarde[sauvegardeChoisie][4]=""+pokemonJoueur.PATK;
                laSauvegarde[sauvegardeChoisie][5]=pokemonJoueur.TypePoke;
                laSauvegarde[sauvegardeChoisie][6]=""+pokemonJoueur.niveau;
                laSauvegarde[sauvegardeChoisie][7]=attaquesDispo[0];
                laSauvegarde[sauvegardeChoisie][8]=attaquesDispo[1];
                laSauvegarde[sauvegardeChoisie][9]=attaquesDispo[2];
                laSauvegarde[sauvegardeChoisie][10]=attaquesDispo[3];
                laSauvegarde[sauvegardeChoisie][11]=""+xp;
                laSauvegarde[sauvegardeChoisie][12]=""+palierxp;
                saveCSV(laSauvegarde,cheminCSV+"Sauvegarde.csv");
                
            }
        }
    
  }
  void algorithm(){
      DebutDeJeu();
  }
}
