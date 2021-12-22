package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * TARRANT County, TX (B)
 */
public class TXTarrantCountyBParser extends DispatchA19Parser {

  public TXTarrantCountyBParser() {
    super("TARRANT COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "dispatch@nrhfd.com,DISPATCH,flexripnrun@nrhtx.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
 }
