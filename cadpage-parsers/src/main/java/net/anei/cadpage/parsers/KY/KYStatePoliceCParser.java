package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class KYStatePoliceCParser extends DispatchA24Parser {

  public KYStatePoliceCParser() {
    this("");
  }

  KYStatePoliceCParser(String defCity) {
    super(defCity, "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
