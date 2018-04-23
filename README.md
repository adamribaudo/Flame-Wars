# Introduction

This is a simple and incomplete implementation of an in-process card game (developed by others) that I've named Flame Wars. Currently, two opponents play N rounds of the game and the output of each turn is recorded in a CSV. Each player makes its choices randomly and in the following order:

1. Player randomly decides whether to draw or play a card
2. If a player draws, player randomly decides whether to play that card as a point, action, effect, or scuttle
3. The opponent then randomly decides whether or not to counter that move (if they have a 6)

# TODO

There are several missing pieces to this game at the moment:

* Scuttling only works for face cards, not point cards
* No win conditions for scoring 3 of a kind
* Incomplete implementation of Jacks. Jacks can pass a point card back and forth, but after the first pass the game doesn't correctly understand who has the point card.
* The output log captures the final state of the turn, but doesn't take into account counter-moves related to playing a 6