  /**
   * Variance refers to the subtyping relationship between generic types.
   */

  /**
   * Ingredient is a super  type of { Tomato, Potato, Chili }
   * { Tomato, Potato, Chili } are subtypes of Ingredient.
   */
  trait Food
  trait Ingredient extends Food {
    def delicious: Boolean
  }
  case object Tomato extends Ingredient { override def delicious: Boolean = false }
  case object Potato extends Ingredient { override def delicious: Boolean = true }
  case object Chili extends Ingredient { override def delicious: Boolean = true }

  /**
   * Relations between types sample
   */
  val chili: Chili.type  = Chili
  val ingredient: Ingredient = chili
  // Ingredient is not a subtype of Chili - broken, wrong relation is forbidden
  //  val c: Chili.type = ingredient
  println(s"chili ClassOf: $ingredient")
  println(s"ingredient ClassOf: $ingredient")

  /**
   * "Nothing" is a subtype of all data types incl. Ingredient, Tomato etc.
   * "Any" is a supertype of all data types incl. Ingredient, Tomato etc.
   */
  val a: Any = ingredient
  val a1: Any = chili
  val a2: Any = Tomato.getClass
  val a3: Any = 1
  val a4: Any = "sample"

  // Can't get Nothing value easily, so next few lines have to be commented out
//  val void: Nothing = ??? // cannot have this value, comes usually from Exception
//  val b: Chili.type  = void
//  val b1: 12  = void

  /**
   * Let's add "delicious" method to Ingredient
   *
   * Also, let's come up with a method returning only delicious ingredients.
   * It happens that I only put Chili there, so I would like to have a List[Chili] as a result
   */
  def filterDelicious(ingredients: List[Ingredient]): List[Ingredient] = ingredients.filter(_.delicious)
  val result: List[Ingredient] = filterDelicious(List(Chili, Chili))

  /**
   * Upper bound syntax for subtyping: "A <: Ingredient"
   */
  def filterDeliciousGen[A <: Ingredient](ingredients: List[A]): List[A] = {
    ingredients.filter(_.delicious)
  }
  val resultGen = filterDeliciousGen[Chili.type](List(Chili, Chili))

  /**
   * Lower bound syntax for supertyping: "A >: Ingredient"
   *
   * Also, introducing Food trait
   */
  def filterDeliciousGen2[A >: Ingredient](ingredients: List[A]): List[A] = {
    // this is not working, as A could be a supertype of Ingredients and not have "delicious" method
//    ingredients.filter(_.delicious)
    ingredients.collect{
      case i: Ingredient => i
    }.filter(_.delicious)
  }
  val resultGen2: List[Ingredient] = filterDeliciousGen(List(Chili, Tomato))
  val resultGen3: List[Food] = filterDeliciousGen(List(Chili, Tomato)) // this is interesting case, see below

  /**
   * Variance basics
   *
   * DEF:
   * Variance - defines how subtyping between more complex types relates to subtyping
   * between their components.
   * e.g.:
   * S relation T -> Complex[S] relation Complex[T]
   *
   * Invariant:      A    default
   * A <: B -> Box[A] is unrelated to Box[B]
   *
   * Covariant:     +A    preserves subtyping relationship
   * A <: B -> Box[A] <: Box[B], HENCE Chili <: Ingredient -> Box[Chili] -> Box[Ingredient]
   * * EXAMPLE:
   * * Covariance means that we cannot put pamars of type A in method parameters, but we can put them
   * * in return types
   *
   * Contravariant: -A    preserves
   * A <: B -> Box[B] <: Box[A], HENCE Chili <: Ingredient -> Box[Ingredient] <: Box[Chili]
   * or perhaps better example (as there aren't that many for contravariance, as for covariance)
   * Cat <: Animal -> PrettyPrinter[Animal] -> PrettyPrinter[Cat]
   * PrettyPrinter[-T] defined as contravariant, then we can pass it to below method
   * def print(p: PrettyPrinter[Cat], cat: Cat) = p.print(cat)
   * EXAMPLE:
   * Contravariance means that we can put pamars of type A in method parameters, but we cannot put them
   * in return types
   */
  // doesn't compile, as Box[A] should be Box[+A]
//  trait Box[A]
//  val boxChili: Box[Chili.type] = ???
//  val boxI: Box[Ingredient] = boxChili

  /**
   * Examples:
   *
   * Covariant: Food/Ingredient
   * val resultGen3: List[Food] = filterDeliciousGen(List(Chili, Tomato))
   *
   * Contravariant
   */
  trait Chef[-A] { def cook(a: A): Unit }
  val chefAny: Chef[Ingredient] = ???
  val chefChili: Chef[Chili.type] = chefAny

  /**
   * Famous Function example
   * Function[-A, +B]
   */
  def eat(a: Ingredient): Nothing = ???
  eat(Chili)

  // this is v. important example
  val f: Ingredient => Nothing = eat
  val g: Chili.type => Int = f

  /**
   * Scala List implementation explained
   */
  // below doesn't compile
//  trait List1[+A] {
//    def ::(a: A): List[A]
//  }

  // below works, because List1 is covariant, so it accepts Chili into Ingredient type.
  // the type serving as conversation starter is Chili of course
  val l1: List[Chili.type] = ???
  val l2: List[Ingredient] = l1

  // if I want to prepend Tomato to already existing list of Chilis, then compiler prevents me from
  // doing so, I cannot add Tomato type to existing Chilis list. Tomato and Chili are not related,
  // even though they are sibling in type definition at the beginning of this file
  val l11: List[Chili.type] = ???
  val l12: List[Ingredient] = Tomato :: l11

  // necessary amendment to List1 trait so that it works with above example
  trait List2[+A] {
    def ::[A1 >: A](a: A1): List2[A1]
  }

  val l21: List[Chili.type] = ???
  val l22: List[Ingredient] = l21.::[Ingredient](Tomato: Ingredient)


  /**
   * Again, basic rule of thumb: input contravariant (-) and output covariant (+)
   * ZIO[-R, +E, +A]
   *  R => E
   *  R =>
   *  R =>Either[E, A]
   */

