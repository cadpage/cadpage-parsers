package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;



public class PAPerryCountyParser extends DispatchArchonixParser {
  
  public PAPerryCountyParser() {
    super(CITY_CODES, "PERRY COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "@perryco.org";
  }
  

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
      "01",   "BLAIN",
      "02",   "BUFFALO TWP",
      "03",   "CARROLL TWP",
      "04",   "CENTRE TWP",
      "05",   "DUNCANNON",
      "06",   "GREENWOOD TWP",
      "07",   "HOWE TWP",
      "08",   "JACKSON TWP",
      "09",   "JUNIATA TWP",
      "10",   "LANDISBURG",
      "11",   "LIVERPOOL",
      "12",   "LIVERPOOL TWP",
      "13",   "MARYSVILLE",
      "14",   "MILLER TWP",
      "15",   "MILLERSTOWN",
      "16",   "NEW BLOOMFIELD",
      "17",   "NEW BUFFALO",
      "18",   "NEWPORT",
      "19",   "N.E. MADISON TWP",
      "20",   "OLIVER TWP",
      "21",   "PENN TWP",
      "22",   "RYE TWP",
      "23",   "SAVILLE TWP",
      "24",   "S.W. MADISON TWP",
      "25",   "SPRING TWP",
      "26",   "TOBOYNE TWP",
      "27",   "TUSCARORA TWP",
      "28",   "TYRONE TWP",
      "29",   "WATTS TWP",
      "30",   "WHEATFIELD TWP",
      "32",   "DELAWARE TWP JC",
      "33",   "GREENWOOD TWP JC",
      "34",   "SUSQUEHANNA TWP JC",

      "51",   "",
      "52",   "",
      "55",   ""
      
      
  });

}
