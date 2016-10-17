package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXTexasCityParser extends DispatchOSSIParser {
  
  public TXTexasCityParser() {
    super(CITY_CODES, "TEXAS CITY", "TX", 
          "( CANCEL ADDR CITY! | FYI ID? CALL ( PLACE ADDR/Z CITY! | ADDR/Z CITY! | PLACE? ADDR! ) ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@texas-city-tx.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BACL", "BACLIFF",
      "DICK", "DICKINSON",
      "LAMA", "LA MARQUE",
      "SANL", "SAN LEON",
      "TEXA", "TEXAS CITY"
  });

}
