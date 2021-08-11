
import groovy.lang.Closure

class JumpBlock {
	def JumpBlock() {
		// Do nothing?
	}
	def compile() {
		return new this.Chain()
	}
	class Chain() {
		def carr
		def Chain() {
			this.carr = []
		}
		def block(def checkTable, Closure c) {
			this.carr.add([
				'check':checkTable,
				'func':c
			])
		}
		def runChecks() {
			def r = []
			for (IChecker a : this.carr) {
				r.add(a['check'].check())
			}
			return r
		}
	}
	
	static interface IChecker {
		IChecker add(String s, Closure c)
		Map check()
	}
	static class CheckTable implements IChecker {
		def table
		def Checks() {
			this.table = [:]
		}
		def add(String s, Closure c) {
			this.table[s] = c
			return this
		}
		Map check() {
			def t = Result()
			for (k in this.table) {
				t.set(k,this.table[k]())
			}
			return t
		}
		static class Result {
			State _state
			def state = Result.class.getDeclaredField('_state')
			def table
			
			def Result() {
				this.fails = 0
				this.table = [:]
			}
			
			def set(String k, def v) {
				this.fails += v ? 0 : 1
				this.table[k] = v
				// Set current state.
				this.state = State.GREEN
				if (this.fails > 0)
					this.state = State.YELLOW
				if (this.fails == this.table.size())
					this.state = State.RED
				return this
			}
			
			static enum State {
				RED, YELLOW, GREEN
			}
		}
	}
}

