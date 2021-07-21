package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTLitchfieldCountyParser extends GroupBestParser {

  public CTLitchfieldCountyParser() {
    super(new CTLitchfieldCountyAParser(),
          new CTNewMilfordParser(),
          new CTLitchfieldCountyBParser(),
          new CTLitchfieldCountyCParser());
  }

  @Override
  public String getLocName() {
    return "Litchfield County, CT";
  }

  public static void fixCity(Data data) {
    if (data.strCity.equals("HEMLOCK ROXBURY")) data.strCity = "ROXBURY";
    if (data.strCity.endsWith(" MA")) {
      data.strState = "MA";
      data.strCity = data.strCity.substring(0,data.strCity.length()-3).trim();
    }
    else if (data.strCity.endsWith(" NY")) {
      data.strState = "NY";
      data.strCity = data.strCity.substring(0,data.strCity.length()-3).trim();
    }
  }

  static String[] CITY_LIST = new String[]{
    "BANTAM",
    "BARKHAMSTED",
    "BETHLEHEM",
    "BRIDGEWATER",
    "BURLINGTON",
    "CANAAN",
    "CANTON",
    "COLEBROOK",
    "CORNWALL",
    "GAYLORDSVILLE",
    "GOSHEN",
    "GRANBY",
    "HARTFORD",
    "HARTLAND",
    "HARWINTON",
    "KENT",
    "LITCHFIELD",
    "MIDDLEBURY",
    "MORRIS",
    "NEW FAIRFIELD",
    "NEW HARTFORD",
    "NEW MILFORD",
    "NEW PRESTON",
    "NEWTOWN",
    "NORFOLK",
    "NORTH CANAAN",
    "NORTHWEST HARWINTON",
    "OAKVILLE",
    "PLYMOUTH",
    "ROXBURY",
    "ROXBURY",
    "SALISBURY",
    "SANDISFIELD",
    "SHARON",
    "SHEFFIELD",
    "SHERMAN",
    "SOUTH KENT",
    "TERRYVILLE",
    "THOMASTON",
    "TORRINGTON",
    "WARREN",
    "WASHINGTON",
    "WATERBURY",
    "WATERTOWN",
    "WINCHESTER",
    "WINSTED",
    "WOODBURY",
    "WOLCOTT",

    "GRANVILLE MA",
    "NEW MARLBORO MA",
    "SANDISFIELD MA",
    "SHEFFIELD MA",
    "TOLLAND MA",

    "AMENIA NY",
    "DOVER NY",
    "MILLERTON NY",
    "WASSAIC NY"

  };
}
