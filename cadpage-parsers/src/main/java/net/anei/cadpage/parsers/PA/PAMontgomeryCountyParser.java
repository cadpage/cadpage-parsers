package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Montgomery county, Pa
 */


public class PAMontgomeryCountyParser extends GroupBestParser {

  public PAMontgomeryCountyParser() {
    super(new PAMontgomeryCountyAParser(),
           new PAMontgomeryCountyBParser(),
           new PAMontgomeryCountyCParser(),
           new PAMontgomeryCountyDParser(),
           new PAMontgomeryCountyEParser(),
           new PAMontgomeryCountyFParser(),
           new PAMontgomeryCountyGParser(),
           new PAMontgomeryCountyKParser());
  }

  public static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ABGN", "ABINGTON TWP",
      "AMBL", "AMBLER",
      "ARDM", "ARDMORE",
      "BCYN", "BALA CYNWYD",
      "BEWD", "DELAWARE COUNTY",   // ????
      "BGPT", "BRIDGEPORT",
      "BHIL", "",             // Bala Cynwyd in Lower Merion Twp
      "BMWR", "BRYN MAWR",
      "BRYA", "BRYN ATHYN",
      "CHEL", "CHELTENHAM TWP",
      "CLGV", "COLLEGEVILLE",
      "CONS", "CONSHOHOCKEN",
      "DGLS", "DOUGLASS TWP",
      "EGRN", "EAST GREENVILLE",
      "ELPK", "ELKINS PARK",
      "ENOR", "EAST NORRITON TWP",
      "FRCN", "FRANCONIA TWP",
      "GHFM", "LOWER MERION TWP",  // until we learn better...
      "GLAD", "GLADWYNE",
      "GLSD", "GLENSIDE",
      "GRLN", "GREEN LANE",
      "HATB", "HATBORO",
      "HAVT", "HAVERFORD TWP",   // in delaware county
      "HORS", "HORSHAM TWP",
      "HTFB", "HATFIELD",
      "HTFT", "HATFIELD TWP",
      "HVRD", "HAVERFORD",
      "JENK", "JENKINTOWN",
      "LAMT", "LA MOTT",
      "LANS", "LANSDALE",
      "LFRE", "LOWER FREDERICK TWP",
      "LGWY", "LOWER GWYNEDD TWP",
      "LMER", "LOWER MERION TWP",
      "LMOR", "LOWER MORELAND TWP",
      "LMRK", "LIMERICK TWP",
      "LPOT", "LOWER POTTSGROVE TWP",
      "LPRO", "LOWER PROVIDENCE TWP",
      "LSAL", "LOWER SALFORD TWP",
      "LVRK", "LAVEROCK",
      "LWGN", "",                 // Elkins,Wyncote in Cheltenham Twp
      "MARL", "MARLBOROUGH TWP",
      "MERN", "MERION",
      "MONT", "MONTGOMERY TWP",
      "MRPK", "MELROSE PARK",
      "MYTWN","MY DEMO TOWN",
      "NARB", "NARBETH",
      "NCOV", "NORTH COVENTRY TWP",  // In Chester County
      "NHAN", "NEW HANOVER TWP",
      "NRSN", "NORRISTOWN",
      "NWAL", "NORTH WALES",
      "OVBK", "OVERBROOK",
      "OVHL", "OVERBROOK HILLS",
      "PNBG", "PENNSBURG",
      "PERK", "PERKIOMEN TWP",
      "PLYM", "PLYMOUTH TWP",
      "PNVY", "PENN VALLEY",
      "PNWN", "PENN WYNNE",
      "POTT", "POTTSTOWN",
      "RDHL", "RED HILL",
      "RKLG", "ROCKLEDGE",
      "ROMT", "ROSEMONT",
      "RYFD", "ROYERSFORD",
      "SALF", "SALFORD TWP",
      "SCHW", "SCHWENKSVILLE",
      "SKPK", "SKIPPACK TWP",
      "SOUD", "SOUDERTON",
      "SPRG", "SPRINGFIELD TWP",
      "TLFD", "TELFORD",
      "TRPP", "TRAPPE",
      "TWMC", "TOWAMENCIN TWP",
      "UDUB", "UPPER DUBLIN TWP",
      "UFRE", "UPPER FREDERICK TWP",
      "UGWY", "UPPER GWYNEDD TWP",
      "UHAN", "UPPER HANOVER TWP",
      "UMER", "UPPER MERION TWP",
      "UMOR", "UPPER MORELAND TWP",
      "UPOT", "UPPER POTTSGROVE TWP",
      "UPRO", "UPPER PROVIDENCE TWP",
      "USAL", "UPPER SALFORD",
      "WCON", "WEST CONSHOHOCKEN",
      "WHPN", "WHITPAIN TWP",
      "WMSH", "WHITEMARSH TWP",
      "WNOR", "WEST NORRITON",
      "WORC", "WORCESTER TWP",
      "WPOT", "WEST POTTSGROVE",
      "WYCT", "WYNCOTE",
      "WYNN", "WYNNEWOOD",
      "VILL", "VILLANOVA",

      "BECO", "BERKS COUNTY",
      "BUCO", "BUCKS COUNTY",
      "CHCO", "CHESTER COUNTY",
      "DECO", "DELAWARE COUNTY",
      "LECO", "LEHIGH COUNTY"
  });

  public static final Properties MAP_CITIES = buildCodeTable(new String[]{
      "OVERBROOK HILLS",      "LOWER MERION TWP"
  });

}
