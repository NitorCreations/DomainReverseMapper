package com.iluwatar.urm.presenters;

import static org.junit.Assert.assertTrue;

import org.junit.Test;



public class PresenterTest {

  @Test
  public void parseShouldReturnCorrectPresenter() {
    Presenter presenter = Presenter.parse("graphviz");
    assertTrue(presenter.getClass().getSimpleName().equals("GraphvizPresenter"));
    presenter = Presenter.parse("plantuml");
    assertTrue(presenter.getClass().getSimpleName().equals("PlantUmlPresenter"));
    presenter = Presenter.parse("mermaid");
    assertTrue(presenter.getClass().getSimpleName().equals("MermaidPresenter"));
  }
}
