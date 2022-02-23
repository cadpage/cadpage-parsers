package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class MSOktibbehaCountyParser extends DispatchA46Parser {

  public MSOktibbehaCountyParser() {
    super("OKTIBBEHA COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "Oktibbeha@911.com,oktibbehaema-911@pagingpts.com";
  }
}
