package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class ILMcLeanCountyParser extends DispatchA63Parser {

  public ILMcLeanCountyParser() {
    super(CITY_LIST, "MCLEAN COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "MetcomCAD@mcleancountyil.gov";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BLOOMINGTON",
      "CHENOA",
      "EL PASO",
      "LE ROY",
      "LEXINGTON",

      // Town
      "NORMAL",

      // Villages
      "ANCHOR",
      "ARROWSMITH",
      "BELLFLOWER",
      "CARLOCK",
      "COLFAX",
      "COOKSVILLE",
      "DANVERS",
      "DOWNS",
      "ELLSWORTH",
      "GRIDLEY",
      "HEYWORTH",
      "HUDSON",
      "MCLEAN",
      "SAYBROOK",
      "STANFORD",
      "TOWANDA",

      // Census-designated place
      "SHIRLEY",
      "TWIN GROVE",

      // Other unincorporated communities
      "BARNES",
      "BENTOWN",
      "BLOOMINGTON HEIGHTS",
      "COVELL",
      "CROPSEY",
      "FLETCHER",
      "FUNKS GROVE",
      "GILLUM",
      "HENDRIX",
      "HOLDER",
      "KERRICK",
      "LAURETTE",
      "LYTLEVILLE",
      "MEADOWS",
      "MERNA",
      "OSMAN",
      "PADUA",
      "RANDOLPH",
      "SABINA",
      "SHIRLEY",
      "WATKINS",
      "WEEDMAN",
      "WESTON",
      "YUTON",

      // Townships
      "ALLIN",
      "ANCHOR",
      "ARROWSMITH",
      "BELLFLOWER",
      "BLOOMINGTON",
      "BLOOMINGTON CITY",
      "BLUE MOUND",
      "CHENEYS GROVE",
      "CHENOA",
      "CROPSEY",
      "DALE",
      "DANVERS",
      "DAWSON",
      "DOWNS",
      "DRY GROVE",
      "EMPIRE",
      "FUNK'S GROVE",
      "GRIDLEY",
      "HUDSON",
      "LAWNDALE",
      "LEXINGTON",
      "MARTIN",
      "MONEY CREEK",
      "MOUNT HOPE",
      "NORMAL",
      "OLD TOWN",
      "RANDOLPH",
      "TOWANDA",
      "WEST",
      "WHITE OAK",
      "YATES",

      // Ghost towns
      "ALLIN",
      "BENJAMINVILLE",
      "KUMLER"
  };

}
