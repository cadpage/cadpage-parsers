package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXWilsonCountyParser extends DispatchA19Parser {

  public TXWilsonCountyParser() {
    super(CITY_CODES, "WILSON COUNTY", "TX");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "no-reply@wilsoncountytx.gov,@alert.active911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "8874 FM 775",                          "+29.277135,-98.181223",
      "8990 FM 775",                          "+29.275624,-98.182019"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AD",  "ADKINS",
      "EM",  "ELMENDORF",
      "FC",  "FALLS CITY",
      "FLV", "FLORESVILLE",
      "GUA", "GUADALUPE COUNTY",
      "LAV", "LA VERNIA",
      "MC",  "MCCOY",
      "NX",  "NIXON",
      "OOC", "OUT OF COUNTY",
      "PA",  "PANDORA",
      "PL",  "PLEASANTON",
      "POT", "POTH",
      "SA",  "SAN ANTONIO",
      "SEG", "SEGUIN",
      "SS",  "SUTHERLAND SPRINGS",
      "STO", "STOCKDALE"

  });


}
