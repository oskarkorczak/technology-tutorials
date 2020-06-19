# ScalaTest


## Running tests


### Bash shell
`sbt test` - run all tests in the project. It also starts new JVM process each time.

`sbt "testOnly HelloWorldSpec"` - test only particular test class, skipping the rest. It also starts new JVM process each time.

### Sbt shell
Start sbt shell by invoking `sbt` in command line. While being in sbt 
shell, it is possible to run only below commands to run tests. The sbt JVM is kicked off once. 

`test` - run all tests. 

`testOnly HelloWorldSpec` - run only particualr `HelloWorldSpec` test.

`testOnly Hello*` - run tests matching wildcard `Hello*`.

`testOnly *World*` - run all tests matching pattern `*World*`.

`~testOnly *Hello*` - continuously runs all tests matching the `*Hello*` pattern. **It is really handy and works well.**

## Test styles

### FlatSpec
Specification text and tests are in a flat structure. Designed for those who wanth to move from xUnit to BDD style tests. 

```
class HelloWorldSpec extends AnyFlatSpec {
	behavior of "Hello World"				(specification text)
	
	it should "start with 'Hello'" in {		(test)
		assert("Hello World".startsWith("Hello"))
	}
}
```

#### Structure
Sample test stub:

`"The Hello World" should "start with 'Hello'" in {}`

Stub structure:

`"The Hello World"` - subject or system under test (SUT)

`"start with 'Hello'"` - rest of the test description

`should` - sample separator between `subject` and `rest of the test description`. There are alternative separators, which could be used:

* `should` form `ShouldVerb` trait
* `must` form `MustVerb` trait
* `can` form `CanVerb` trait


#### Lifecycle
```
class HelloWorldSpec extends AnyFlatSpec {
	`"The Hello World" should "start with 'Hello'" in {}`
}
``` 
There are two phases:

##### Registration phase
Uses class `ItVerbString` (manifesting as `in` in the test) to separate `Test Sentence` from `Test Body`, where:

* 	`"The Hello World" should "start with 'Hello'"` - Test Sentence
*  	`{}` - Test Body (all assert statements are written here)

##### Ready phase
Phase used for running all registered tests. It is impossible to register more tests post-registration phase.


#### Multiple tests
There are cases when more than one test is needed for the subject (SUT). 

```
"The Hello World" should "start with 'Hello'" in {}
"The Hello World" should "end with 'World'" in {}
```
There is a subject duplication in both tests. 

##### First test registers subject
Start writing first test with subject name and in the following tests use `it`:

```
"The Hello World" should "start with 'Hello'" in {}
it should "end with 'World'" in {}
it should "have length > 0" in {}
```
In case you need to find what the subject is, just vertically scan up from given `it` to closest subject description. 

##### `behavior of` registers subject
Subject registration happens through statement `behavior of`. In this way, all subsequent `it` statements refer to subject registered via `behavior of` one. 

```
behavior of "Hello World"
it should "start with 'Hello'" in {}
it should "end with 'World'" in {}
it should "have length > 0" in {}
```
To find out what subject is used, vertically scan up the `it` usage until you come across `behavior of` defining the subject. 



### FunSuite
Good for teams coming from xUnit frameworks (jUnit etc.).

```
class HelloWorldFunSuiteSpec extends AnyFunSuite {
	test("A String 'Hello World' should start with 'Hello'") {
		assert("Hello World".startsWith("Hello"))
	}
}
```

### FunSpec
Designed for teams coming from Ruby RSpec and like BDD style of testing. 

```
class HelloWorldFunSpec extends AnyFunSpec {
	describe("A String 'Hello World' String") {
		it("should start with 'Hello'") {	
			assert("Hello World".startsWith("Hello"))
		}
	}
}
```

### FeatureSpec
Usually used for acceptance testing.

```
class HelloWorldFunSpec extends AnyFeatureSpec with GivenWhenThen {
	info("As a developer")
	info("I want to test String")
	feature("A String") {
		scenario("When a user provides 'Hello World' as input") {
			Then("The input must start with 'Hello'")
			assert("Hello World".startsWith("Hello"))
		}
	}
}
```

### WordSpec
Generally for teams coming from spec or spec2 library. It enforces a high degree of discipline when writing their specifications.

### FreeSpec
For teams experienced in BDD style.

### PropSpec
Designed for teams wanting to perform Property-Based testing. Test checks the property of the production code it has to adhere to. Then test case automatically generates test data to check whether production code is valid against assumed property. 



## Assertions

ScalaTest has three variations of assertions:

* `assertResult` - validate expected vs actual
* `assertThrows` - validate exceptions
* `assert` - validate everything else

### Assert

```
class DollarsSpec extends AnyFlatSpec {
	behavior of "A Dollar"
	
	it should "create a correct Dollar object for number 10 as input" in {
		val tenDollars = Dollars(10)
		
		assert("$10" === tenDollars.toString)
}
```
In sbt console type `testOnly *DollarsSpec` to run tests. 

It is very important to use wildcard pattern `*` before the name of the test class as it has to be fully qualified name otherwise. So either:

* `testOnly assertions.DollarsSpec` - assuming `DollarSpec` is located in `assertions` test package
* `testOnly *DollarsSpec` - to use wildcard pattern and sort the problem

ScalaTest always run one test suite, where there is min. single test case. 


### AssertResult

```
class DollarsSpec extends AnyFlatSpec {
	behavior of "A Dollar"
	
	it should "correctly add two Dollars amounts" in {
		val tenDollars = Dollars(10)
		val twoDollars = Dollars(2)
		
		assertResult("$12") {
			(tenDollars + twoDollars).toString
		}
	}
	
	it should "correctly identify that $4 is $4" in {
		val fourDollars = Dollars(4)
		
		assertResult(true) {
			fourDollars === Dollars(4)
		}
	}
}
```
Run tests `testOnly *DollarSpec`


### AssertThrows
```
class DollarsSpec extends AnyFlatSpec {
	behavior of "A Dollar"
	
	it should "throw an exception when an invalid integer is provided" in {
		assertThrows[ArithmeticException] {
			Dollars(10/0)
		}
	}
}
```
Run tests `testOnly *DollarSpec`. 


#### Intercept usage

```
class EmailSpec extends AnyFlatSpec {
	behavior of "Email"
	
	it should "intercept the correct error message when no '@' symbol is provided" in {
		val exception = intercept[IllegalArgumentException] { Email("google.com") }
		assert(exception.isInstanceOf[IllegalArgumentException])
		assert(exception.getMessage.contains("does not contain '@'"))
	}
}
```
`intercept` catches the exception allowing to interogate the object more thoroughly. 

### fail()
`fail()` funciton could be used in few ways. 

#### PR review

Imagine you do a pull request (PR) review for your colleague in provided branch. Even though all tests are passing, you noticed there are properties of an object, which are not tested. You may highlight the problem by using `fail()` function.

```
class EmailSpec extends AnyFlatSpec {
	behavior of "Email"
	
	it should "return another Email object for another valid String" in {
	assertResult("jim") {
		Email("jim@google.com").localPart
	}
	fail("Test all properties for Email object")
}
```
Run tests `testOnly *EmailSpec` and observe test failing with message mentioned above. 


#### Drafting all test cases first
All necessary test cases in the test class could be drafted like below and then successively one by one implemented (which means `fail()` are phased out slowly). 

```
class EmailSpec extends AnyFlatSpec {
	behavior of "Email"
	
	it should "return another Email object for another valid String" in {
	fail("Not Implemented Yet")
}

	it should "return Email object for valid String" in {
	fail("Not Implemented Yet")
}
```
Works nicely with sbt continuous trigger option `~testOnly *EmailSpec`.


##### Ignore keyword
Alternatively, it is possible to use `ignore` keyword insted of `it` to suppress test case execution, which we will see in test suite summary in sbt. 

```
class EmailSpec extends AnyFlatSpec {
	behavior of "Email"
	
	ignore should "return another Email object for another valid String" in {
	fail("Not Implemented Yet")
}

	ignore should "return Email object for valid String" in {
	fail("Not Implemented Yet")
}
```
Test are not failing per se, but are ignored. 


### Preconditions
Sometimes it makes sense to test a scenario only if certain preconditions are met. 


Let's assume we have an API call:

```
class DollarsSpec extends AnyFlatSpec {
	behavior of "A Dollar"
	
	it should "have every dollar more than 0" in {
		// assume API call
		val dollars: List[Dollars] = List(Dollars(1), Dollars(100), Dollars(20))
		
		dollars.foreach{ d => 
			assert(d.amount > 0)
		}
	}
}
```
Run the test case and observe all test pasisng. Now, what if that API call returns empty list:

```
class DollarsSpec extends AnyFlatSpec {
	behavior of "A Dollar"
	
	it should "have every dollar more than 0" in {
		// assume API call
		val dollars: List[Dollars] = List.empty
		
		dollars.foreach{ d => 
			assert(d.amount > 0)
		}
	}
}
```
Again, run test case and test are passing. Actually they do not pass, because list is empty, which further means that `foreach` loop is simply not executing. 

Better approach is to `assume()` that dollars list is non-empty and only then run test:

```
class DollarsSpec extends AnyFlatSpec {
	behavior of "A Dollar"
	
	it should "have every dollar more than 0" in {
		// assume API call
		val dollars: List[Dollars] = List.empty
		
		assume(dollars.nonEmpty)
		
		dollars.foreach{ d => 
			assert(d.amount > 0)
		}
	}
}
```
If you run test, it will be cancelled, due to precondition being not met. 


### Useful clues (exception messages)
Messages displayed for readibility purposes on sbt console, when test fails. 

#### Assertions

Both `assert()` and `assertResult()` have additional parameter called `clue`, which is displayed when test fails. 

```
assert(email.localPart === "howdy", "expected localPart is 'howdy'")
assertResult("jim", "expected localPart is 'jim'") {
	Email("jim@google.com").localPart
}
```

For `assertThrows` we have to utilize `withClue()` clause. 

```
it should "throw an exception when an email does not contain '@' symbol" in {
	withClue("expected exception since email does not have '@'") {
		assertThrows[IllegalArgumentException] {
			Email("jim.com")
		}
	}
}
```

#### Preconditions
Preconditions can also have clues, when condition is not met. 

```
class DollarsSpec extends AnyFlatSpec {
	behavior of "A Dollar"
	
	it should "have every dollar more than 0" in {
		// assume API call
		val dollars: List[Dollars] = List.empty
		
		assume(dollars.nonEmpty, "The dollars coming from API should not be empty")
		
		dollars.foreach{ d => 
			assert(d.amount > 0)
		}
	}
}
```


## Matchers
Another way to perform assertions is using domain specific language (DSL). It creates the flow of the sentence, which reads nicely: 

```
currency1 shouldEqual currency2
wallet should not contain (hundredUsd)
customerIds should have size 2
```

Matchers could be either imported:

```
import org.scalatest._
import Matchers._

class EmailSpec extends AnyFlatSpec {
	// matchers are available now
}
```

or could be mixed in `Matchers` trait, which is the **PREFERRED** way:


```
import org.scalatest._

class EmailSpec extends AnyFlatSpec with Matchers {
	// matchers are available now
}
```

### Equality
There are few ways of testing equality with Matchers. 

#### should equal
`left should equal (right)`

This syntax requires the `org.scalactic.Equality[L]` (aka equality conversions) to be provided implicitely or explicitely. ScalaTest provides basic implicit equality conversions, so we do NOT need to provide them for simple types:

```
org.scalactic.Equality[String]			(implicit conversion provided)
"Hello" should equal ("Hello")

org.scalactic.Equality[Int]				(implicit conversion provided)
100 should equal (100)
```

ScalaTst basic equality types:

| Type (T)   | Equality[T]      | Example              |
| :--------: | :--------------: | :------------------: | 
| String     | Eqality[String]  | `"Hello" == "Hello"` |
| Int        | Eqality[Int]     | `100 == 100`         |
| Boolean    | Eqality[Boolean] | `false == false`     |
| Double     | Eqality[Double]  | `10.01 == 10.01`     |
| Char       | Eqality[Char]    | `'A' == 'A'`         |
| Case Class | Eqality[Apple]   | ```apple == apple (assuming case class is defined)```|

There is an exception in `==` comparision for `Arrays`, it looks for **instance**, not **structural** comparision:

`Array(100, 200) == Array(100, 200)		(result: false)`

However, `should equal` is searching for **structural** comparision, so below code results in `true`:
`Array(100, 200) should equal Array(100, 200)		(result: true)`

#### other syntaxes for comparison
Below group allows `customizable equality` through `Equality[T]` class:

```
"hello" should equal("hello")
"hello" should ===("hello")		(also checks type at compile time)
"hello" should be("hello")
```

However, the group below does **NOT** allow any customization of equality and therefore it is faster to compile below checks, as there is no need to look for `Equality[T]` class:

```
"hello" shouldEqual("hello")	or	"hello" shouldEqual "hello"
"hello" shouldBe("hello")	or	"hello" shouldBe "hello"
```


### String matchers
`MatcherWords.scala` class provides `String` operations for matchers. 

#### Basic
Basic ones are `startWith`, `endWith` and `include`:
```
"hello" should startWith ("h")
"hello" should endWith ("o")
"hello" should include ("e")
```

#### Regex
There are regex matchers too:

```
customer.email.toString should include regex "[a-z]+[@.]com"
customer.dateOfBirth.toString should fullyMatch regex """[0-9]{4}-[0-9]{2}-[0-9]{2}"""
```

### Ordering
Class called `MatcherWords` contains `be` reference to `BeWord` class, which contains ordering relations like: `>`, `<`, `>=` & `<=`. 

```
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OrderingSpec extends AnyFlatSpec with Matchers {
	
	behavior of "Currency conversion cost in comparison"
	
	it should "report equal costs for $10, $10" in {
		val tenUsd: Currency = "10 USD"
		val anotherTenUsd: Currency = "10 USD"
		
		tenUsd.costInDollars.amount should be >= anotherTenUsd.costInDollars.amount
	}
	
	it should "report NZD < USD" in {
		"NZD" should be < "USD"		(Lexicographic ordering)
	}
}
```
Rest of the relation works similarly. 


### Abstract base class
In order to avoid duplication of extending same spec type and set of matchers, all of that could be extracted to `UnitSpec` abstract class:

```
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class UnitSpec extends AnyFlatSpec with Matchers
```

Next, use it in all your unit tests:

`class StringSpec extends UnitSpec { ... } `

### Length and size
Class called `MatcherWords` contains `have` reference to `HaveWord` class, which contains `length` and `size` methods defined:

```
class LengthAndSizeSpec extends UnitSpec {
	
...
customer.first should have length first.length

...
val customers: Seq[String] = List("Alan", "Bob")
customers should have size 2
}
```
Rest of the relation works similarly. 


### Containers' testing
ScalaTest comes with a lot of ready to use code for testing containers. 

`List(1, 2) should contain (1)`

There are basic Scala containers, which already have `org.scalatest.enablers.Containing[L]` class implemented for: 

* `GenTraversable[E]`
* `java.util.Collection[E]`
* `java.util.Map[K, V]`
* `String`
* `Array[E]`
* `Option[E]`

Class called `MatcherWords` contains `contain` reference to `ContainWord` class, which many useful assertions defined:

```
class ContainerSpec extends UnitSpec {
...
val wallet = List(oneUsd, twoUsd, tenCad)
wallet should contain (oneUsd)

...
val wallet = Set(oneUsd, twoUsd, tenCad)
wallet should contain (oneUsd)

...
val wallet = Map[String, Currency] = Map(
	"USD" -> oneUsd,
	"EUR" -> twoEuros,
	"CAD" -> tenCad
)
wallet should contain ("oneUSD" -> oneUsd)

...
val wallet: Set[Currency] = Set(oneUsd, oneUsd, twoEuros, tenCad, tenCad)
wallet should contain oneOf (oneUsd, hundredInr)
wallet should contain oneElementOf List(oneUsd, hundredInr)

...
val wallet: Set[Currency] = Set(oneUsd, oneUsd, twoEuros, tenCad, tenCad)
wallet should contain oneOf (oneUsd, twoEuros)	(this test contains more than one occurance of ccy and it will fail; oneOf means "one and only one")

...
val wallet: Set[Currency] = Set(oneUsd, oneUsd, twoEuros, twoEuros)
wallet should contain noneOf (tenCad, hundredInr)
wallet should contain noneElementOf List(tenCad, hundredInr)
}
```

### Emptyness
Emptyness is tested with `should be` matcher according to below pattern:

`List() should be (empty)`, where `T => Emptiness[T]` is implemented for: 

* `GenTraversable[E]`
* `java.util.Collection[E]`
* `java.util.Map[K, V]`
* `String`
* `Array[E]`
* `Option[E]`

Class called `MatcherWords` contains `be` reference to `BeWord` class, which defines emptiness:

```
class EmptynessSpec extends UnitSpec {
...
customer.last should be (empty)
customer.last shouldBe empty
}
```


### Exceptions
Class called `MatcherWords` contains `be` reference to `BeWord` class, which defines `a`, `an`, `thrownBy`:

```
class ExceptionSpec extends UnitSpec {

	behavior of "Currency during exception"
	
	it should "throw an exception when invalid number is provided in the ccy string" in {
	a [NumberFormatException] should be thrownBy Currency.stringToCurrency("Two USD")
	an [NumberFormatException] should be thrownBy Currency.stringToCurrency("Two USD")
	
	...
	val excpetion = the [NumberFormatException] thrownBy Currency.stringToCurrency("Two USD")
	exception.getMessage should include("Two")

	}
}
```


### Logical operators

```
class LogicalSpec extends UnitSpec {

	behavior of "Currencies as logical and/or"
	
	tenNzd.costInDollars.amount should (be > (0) and be <= (10))
	
	tenNzd.code should (have length (0) or equal ("NZD"))
}
```


### Negations

```
class NegationSpec extends UnitSpec {
	
	currency1 should not be (currency2)
	
	currency1 should not equal (currency2)
	
	val wallet = List(oneUsd, twoEuros, tenCad)
	wallet should not contain (tenNzd)
	
	customerId.toString should not be (empty)
	
	val wallet: List[Currency] = List("1 USD")
	wallet should not be (empty)
	
	tenNzd.code should (have length (0) and not equal ("USD"))
}
```

### Objects comparison

```
customer1 should be theSameInstanceAs customer2
```

### Pattern matching

```
class PatternMatchingSpec extends UnitSpec with Inside {
	
	inside(tenUsd) { case Currency(code, amount, costInDollars) => 
		code should equal ("USD")
		amount should equal (10.0 +- 0.5)
		costInDollars should be > Dollars(12)
	}
	
	... 
	
	tenUsd should matchPattern { case Currency("USD", _, _) }
	
}
```
`Inside` trait allows to make statements about nested object graphs ie interogating object properties. 
 
 
## Fixtures

Fixtures are objects/data that our test depends on. They could be extracted to common place and shared across tests, which substantially shrinks the size of tests.

### Fixture object

```
class ScalaFixtureSpec extends UnitSpec {

	def fixture = new {
		val currency1: Currency = 10 USD"
		val currency2: Currency = 10 USD"
	}
	
	it should "match bla" in {
		val f = fixture
		f.currency1 should equal(f.currency2)
	}
	
	it should "match other bla" in {
		val f = fixture
		f.currency1 should ===(f.currency2)
	}
}
```
Every test uses new, fresh instance of the fixture object. 


### Fixture context object
Good for different tests depending on different test data. 

```
class ScalaFixtureContectObjectSpec extends UnitSpec {
	
	trait ACustomer {
		private val service: CustomerService = new CustomerService {}
		private val customerId: UUID = service.createNewCustomer("nancy", "williams", "nancy@google.com", "1982/06/20")
		val customer: Customer = service.getCustomer(customerId).get
	}

	trait AProduct {
		private val service: ProductService = new ProductService {}
		private val productId: UUID = service.addNewDepositProduct("CoreChecking", 2000, 0.02, 10)
		val product: Deposits = service.getDepositProduct(productId).get
	}
	
	trait ADollars {
		val fiveThousandDollars = Dollars(5000)
	}

	behavior of "ACustomer"	
	it should "return a customer with non-empty email address" in new ACustomer {
		customer.email.toString should not be empty
	}
	
	behavior of "AProduct"
	it should "return a valid product with more 1000 dollars minimum balance requirement" in new AProduct {
		product.minimumBalancePerMonth should be >= Dollars(1000)
	}
	
	behavior of "A Product with some Money"
	it should "return true for 5000 dollars be greater than the product min balance requirement" in new ADollars with AProduct {
		fiveThousandDollars should be > product.minimumBalancePerMonth
	}
}
```
Traits are mixed in with relevant tests which need particular context:
`it should "return a customer with non-empty email address" in new ACustomer {` or `	it should "return true for 5000 dollars be greater than the product min balance requirement" in new ADollars with AProduct {`


### Loan fixture method
Good choice when you need to run a cleanup, not to perform any side effects. 

```
class ScalaLoanFixtureSpec extends UnitSpec {
	
	private val currencies: List[Currency] = List.empty
	
	def createRandomCurrencies: List[Currency] = {
		currencies = List(oneUSD, twoEuros)
	}
	
	def removeRandomCurrencies(): Unit = {
		currencies = List.empty
	}
	
	def withRandomCurrencies(testCode: List[Currency] => Any): Unit = {
		val currencies = createRandomCurrencies
		try {
			testCode(currencies)
		}
		finally removeRandomCurrencies()
	}
	
	behavior of "Testing random currencies with load pattern"	
	it should "have totalCost of Dollars more than $10" in withRandomCurrencies { currencies =>
		currencies.map(_.costInDollars.amount).sum should be > 10
	}
}
``` 
`withRandomCurrencies` created a resource, which was given as a loan to test code. When tesst code is done, the `withRandomCurrencies` takes responsibility of clearing the `currencies` state. 

`withRandomCurrencies` takes `testCode: List[Currency] => Any`, so even when `testCode` throws an exception the `try` block can handle it and still clear the resource. 


### withFixture method
ScalaTest applies `loan fixture` pattern on developer's behalf. 

```
class WithFixtureParamSpec extends FixtureAnyFlatSpec with Matchers {
	
	private val currencies: List[Currency] = List.empty
	
	override protected def withFixture(test: OneArgTest): Outcome = {
		currencies = List(oneUSD, twoEuros)
		
		try {
			withFixture(test.toNoArgTest(FixtureParam(currencies)))
		} finally {
			currencies = List.empty
		}
	}
	
	case class FixtureParam(currencies: List[Currency])
		
	behavior of "Testing random currencies with withFixture"	
	it should "have totalCost of Dollars more than $10" in { f =>
		f.currencies.map(_.costInDollars.amount).sum should be > 10
	}
}
``` 


### BeforeAndAfter

```
class BeforeAndAfterSpec extends UnitSpec with BeforeAndAfter {
	
	private val currencies: List[Currency] = List.empty
	
	before {
		currencies = List(oneUSD, twoEuros)
	}
	
	after {
		currencies = List.empty
	}

	behavior of "Testing random currencies with BeforeAndAfter"	
	it should "have totalCost of Dollars more than $10" in {
		currencies.map(_.costInDollars.amount).sum should be > 10
	}
}
``` 
Each test has an access to different instances of `currencies` due to `before` and `after` blocks. 

### BeforeAndAfterAll

```
class BeforeAndAfterAllSpec extends UnitSpec with BeforeAndAfterAll {
	
	private val currencies: List[Currency] = List.empty
	
	override protected def beforeAll(): Unit {
		currencies = List(oneUSD, twoEuros)
	}
	
	override protected def afterAll(): Unit {
		currencies = List.empty
	}

	behavior of "Testing random currencies with BeforeAndAfter"	
	it should "have totalCost of Dollars more than $10" in {
		currencies.map(_.costInDollars.amount).sum should be > 10
	}
}
``` 
State of `currencies` is shared across all tests. 


### Asynchronous code testing

```
class BeforeAndAfterAllSpec extends AsyncFlatSpec with Matchers {
	
	def addDollars(dollars: Dollars*): Future[Dollars] = Future {
		val totalAmount = dollars.map(dollar => dollar.amount).sum
		Dollars(totalAmount)
	}

	behavior of "Async Dollars"	
	
	it should "have totalCost of Dollars more than $10" in {
		val total: Future[Dollars] = addDollars(Dollars.Zero, Dollars(10), Dollars(2))
		total.map(dollars => dollars.amount shouldBe 12)
	}
}
``` 
`AsyncFlatSpec` provides implicit execution context to run async code. 

`addDollars` sums all the dollars and returns result in the `Future`.

ScalaTest runs test suites in parallel, however all tests within given (ansync?) test suite are run sequentially. In order to have parallel tests in the suite mix in `ParallelTestExecution`. 

**Read about serial/parallel way of execution of tests in async vs regular context more.** 


## Mocking
ScalaTest provides integration with popular mocking tools:

* ScalaMock
* JMock
* EasyMock
* Mockito

ScalaMock approach will be used below. 

### Function mocks
`mockFunction	` is used to mock funtions, which might be passed params to other functions/methods. 

#### Rather artificial example
Goal: Write test for `getCurrency` function using mocks and without passing explicit implementation of `criteria`.

Initial state of the code, which has to be converted to mocking style:

```
class FunctionMockSpec extends FlatSpec with Matchers {

	behavior of "Currency's Mocking"
	
	it should "be able to mock a higher order function for any input argument and any number of times" in {
		val currencies: List[Currency] = List("100 USD", "20 EUR", "1000 CHF", "1 USD")
		
		def getCurrency(criteria: Currency => Boolean): List[Currency] = currencies.filter(criteria)
		
		def criteria: Currency => Boolean = (c: Currency) => c.code == "USD"
		
		getCurrency(crieteria) should have be 2	
	}
}
```

After conversion to mock style:


```
class FunctionMockSpec extends FlatSpec with Matchers with MockFactory {

	behavior of "Currency's Mocking"
	
	it should "be able to mock a higher order function for any input argument and any number of times" in {
		val currencies: List[Currency] = List("100 USD", "20 EUR", "1000 CHF", "1 USD")
		def getCurrency(criteria: Currency => Boolean): List[Currency] = currencies.filter(criteria)
		
		val criteriaMock = mockFunction[Currency, Boolean]
		criteriaMock.expects(*)	.anyNumberOfTimes()
			
		getCurrency(criteriaMock)	
	}
}
```

`mockFunction` - generate a mock for the function (not an object)

`criteriaMock.expects(*)` - expect any type of input (`*`)

`.anyNumberOfTimes()` - needed as `currencies.filter(criteria)` iterates through each item in the container. 


#### A bit better example
```
import org.scalatest.FlatSpec
import org.scalamock.scalatest.MockFactory

class ExampleSpec extends FlatSpec with MockFactory with ...

val m = mockFunction[Int, String]

m expects (42) returning "Forty two" once
```
`MockFactory` needed for `mockFunction`. It also takes the parameters from `Int` to `String`.

Then expectation could be set on mock, which basically means one may teach mock how to behave.

### Proxy mocks
Used to mock only Scala traits or Java interfaces. 

```
import org.scalatest.FlatSpec
import org.scalamock.ProxyMockFactory

class ExampleSpec extends FlatSpec with ProxyMockFactory with ...

val m = mock[Turtle]

m expects 'setPosition withArgs (10.0, 10.0)
m expects 'forward withArgs (5.0)
m expects 'getPosition returning (15.0, 10.0)
```

Expectations could be set on all trait methods. By default, an expectation accepts:

* any arguments and 
* a single call

Following two statements are equivalent `m expects 'forward withArgs (*) once` <==> `m expects 'forward`.

#### Stubs
As a convenience there are also `stubs` supported, for which two following statements are equivalent `m expects 'forward anyNumberOfTimes` <==> `m stubs 'forward`. 


### Expected call count
Mocks can have expected count set. Here are few non-exhaustive examples:

```
((mocked.openDepositAccount _) expects(customerId, *, *)).noMoreThanOnce()

((mocked.openDepositAccount _) expects(customerId, *, *)).atLeastTwice()

((mocked.openDepositAccount _) expects(customerId, *, *)).anyNumberOfTimes()

```

## Tagging
Allows categorising tests into groups and then testing those groups or entire universe of tests excluding that group. 

Create a separate class with `Tag`s definitions.

```
import org.scalatest.Tag

object Slow extends Tag("com.speed.Slow")
object Fast extends Tag("com.speed.Fast")
```

Then tag your tests with relevant tags:

```
...
it should "be able to run fast" taggedAs (Fast) in {
	...
}

...
it should "be able to run slow" taggedAs (Slow) in {
	...
}

...
it should "be able to run with regular speed" in {
	...
}
```

Running all `Fast` tests: `testOnly -- -n com.speed.Fast`, where `-n` means `include the tags`.

Running all `Slow` tests: `testOnly -- -n com.speed.Slow`.

Running all tests except those tagged as `Slow`: `testOnly -- -l com.speed.Slow`, where `-l` means `exlude certain tag`.

Run all tests regardless of tags: `test`.






