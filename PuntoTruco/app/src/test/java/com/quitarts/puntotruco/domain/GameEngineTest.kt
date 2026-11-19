package com.quitarts.puntotruco.domain
import com.quitarts.puntotruco.model.GameState
import org.junit.Assert.assertEquals
import org.junit.Test
class GameEngineTest {
    @Test fun addChangesOnlySelectedTeam() {
        val start=GameState(ours=5,theirs=8)
        assertEquals(start.copy(ours=6),GameEngine.add(start,true))
        assertEquals(start.copy(theirs=9),GameEngine.add(start,false))
    }
    @Test fun undoChangesOnlySelectedTeam() {
        val start=GameState(ours=6,theirs=9)
        assertEquals(start.copy(ours=5),GameEngine.undo(start,true))
        assertEquals(start.copy(theirs=8),GameEngine.undo(start,false))
    }
    @Test fun limitsAreRespected() {
        for(target in listOf(15,18,30)) {
            val full=GameState(ours=target,theirs=0,target=target)
            assertEquals(full,GameEngine.add(full,true))
            assertEquals(full,GameEngine.add(full,false))
            assertEquals(GameState(target=target),GameEngine.undo(GameState(target=target),false))
        }
    }
    @Test fun alternatingOperationsRemainIndependent() {
        var s=GameState()
        s=GameEngine.add(s,true);s=GameEngine.add(s,false);s=GameEngine.add(s,false)
        s=GameEngine.undo(s,true)
        assertEquals(0,s.ours);assertEquals(2,s.theirs)
    }
    @Test fun reachingTargetStopsBothTeams() {
        val finalScore = GameState(ours = 15, theirs = 12, target = 15)
        assertEquals(finalScore, GameEngine.add(finalScore, true))
        assertEquals(finalScore, GameEngine.add(finalScore, false))
    }

    @Test fun undoAtZeroNeverGoesNegative() {
        val empty = GameState(ours = 0, theirs = 0)
        assertEquals(empty, GameEngine.undo(empty, true))
        assertEquals(empty, GameEngine.undo(empty, false))
    }

    @Test fun undoAfterVictoryAllowsContinuing() {
        val finalScore = GameState(ours = 30, theirs = 25, target = 30)
        val reopened = GameEngine.undo(finalScore, true)
        assertEquals(29, reopened.ours)
        assertEquals(26, GameEngine.add(reopened, false).theirs)
    }
}
