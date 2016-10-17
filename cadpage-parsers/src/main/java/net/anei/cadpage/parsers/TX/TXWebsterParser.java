package net.anei.cadpage.parsers.TX;

/*
 * Webster, TX
 * front end to Nassau Bay, TX
 */
public class TXWebsterParser extends TXLaPorteParser {
  
  public TXWebsterParser() {
    super("WEBSTER", "TX");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
}
