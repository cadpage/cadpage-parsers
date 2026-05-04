package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MILivingstonCountyBParser extends DispatchOSSIParser {

  public MILivingstonCountyBParser() {
    super(CITY_CODES, "LIVINGSTON COUNTY", "MI",
          "FYI? ( MUTAID ADDR! " +
               "| ADDR/Z MUTAID SRC CITY! " +
               "| ADDR/Z CITY CALL SRC! " +
               "| CALL ADDR CITY/Y PHONE? X? " +
               ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "pagegate@livgov.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    if (!subject.equals("Text Message") &&
        !body.startsWith(subject)) return false;
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body,  data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MUTAID")) return new CallField("MUTAID", true);
    if (name.equals("CALL")) return new CallField("[A-Z0-9]{2,6}", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("PHONE")) return new PhoneField("\\d{10}", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BRTN", "BRIGHTON",
      "FENT", "FENTON",
      "FOWL", "FOWLERVILLE",
      "GREG", "GREGORY",
      "HART", "HARTLAND",
      "HOLL", "HOLLY",
      "HOWL", "HOWELL",
      "LIND", "LINDEN",
      "PERY", "PERRY",
      "PINK", "PINCKNEY",
      "STKB", "STOCKBRIDGE",
      "WEBB", "WEBBERVILLE",
      "WHIT", "WHITMORE LAKE",
      "ZGRG", "GREGORY",

      // Copied from MILivingstonCountyA
      "BYRN", "BYRON",
      "COHC", "COHOCTAH",
      "DXTR", "DEXTER",
      "FOWL", "FOWLERVILLE",
      "HAMB", "HAMBURG",
      "HART", "HARTLAND",
      "HOLL", "HOLLY",
      "HOWL", "HOWELL",
      "LIND", "LINDEN",
      "LKLD", "LAKELAND",
      "MILF", "MILFORD",
      "PERY", "PERRY",
      "PINK", "PINCKNEY",
      "SLYN", "SOUTH LYON",
      "STKB", "STOCKBRIDGE",
      "WEBB", "WEBBERVILLE",
      "WHIT", "WHITMORE LAKE",
      "ZANN", "ANN ARBOR",
      "ZBYR", "BYRON",
      "ZCHL", "CHELSEA",
      "ZCLS", "CLARKSTON",
      "ZDBG", "DAVISBURG",
      "ZDEX", "DEXTER",
      "ZFEN", "FENTON",
      "ZFMH", "FARMINGTON HILLS",
      "ZFOW", "FOWLERVILLE",
      "ZFRK", "FRANKLIN",
      "ZGRB", "GRAND BLANC",
      "ZGRL", "GRASS LAKE",
      "ZHLD", "HIGHLAND",
      "ZHOL", "HOLLY",
      "ZHRT", "HARTLAND",
      "ZJKS", "JACKSON",
      "ZLAN", "LANSING",
      "ZLIN", "LINDEN",
      "ZMIL", "MILFORD",
      "ZMSN", "MASON",
      "ZMUN", "MUNITH",
      "ZNHD", "NEW HUDSON",
      "ZNOV", "NOVI",
      "ZNVL", "NORTHVILLE",
      "ZPIN", "PINCKNEY",
      "ZPLL", "PLEASANT LAKE",
      "ZPLY", "PLYMOUTH",
      "ZPRY", "PERRY",
      "ZROK", "ROYAL OAK",
      "ZSFD", "SOUTHFIELD",
      "ZSLM", "SALEM",
      "ZSLY", "SOUTH LYON",
      "ZSTB", "STOCKBRIDGE",
      "ZWIL", "WILLIAMSTON",
      "ZWIX", "WIXOM",
      "ZWLK", "WHITE LAKE",
      "ZWML", "WHITMORE LAKE",
      "ZWVL", "WEBBERVILLE",
      "ZYPS", "YPSILANTI"

  });
}
