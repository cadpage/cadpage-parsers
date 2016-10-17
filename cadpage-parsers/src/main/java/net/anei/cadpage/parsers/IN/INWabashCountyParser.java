package net.anei.cadpage.parsers.IN;


import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

/**
 * Wabash County, IN
 */
public class INWabashCountyParser extends DispatchA29Parser {
  
  public INWabashCountyParser() {
    super(CITY_LIST, "WABASH COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@wabashcounty.in.gov";
  }
  
  private static final String[] CITY_LIST = new String[]{

      //cities and towns
      "LA FONTAINE",
      "LAFONTAINE",
      "LAGRO",
      "NORTH MANCHESTER",
      "ROANN",
      "WABASH",

      //Unincorporated towns

      "AMERICA",
      "BOLIVAR",
      "COLLEGE CORNER",
      "DISKO",
      "IJAMSVILLE",
      "LAKETON",
      "LIBERTY MILLS",
      "LINCOLNVILLE",
      "MOUNT VERNON",
      "NEWTON",
      "PIONEER",
      "RICHVALLEY",
      "SERVIA",
      "SOMERSET",
      "SOUTH HAVEN",
      "SPEICHERVILLE",
      "STOCKDALE",
      "SUNNYMEDE",
      "TREATY",
      "URBANA",
      "VALLEY BROOK",

      //Townships

      "CHESTER",
      "LAGRO",
      "LIBERTY",
      "NOBLE",
      "PAW PAW",
      "PLEASANT",
      "WALTZ",
      
      // Grant County
      "MARION"
     
  };
}
