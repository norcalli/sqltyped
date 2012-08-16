package sqltyped

import org.scalatest._
import sqltyped.SqlParser._

class ParserSuite extends FunSuite with matchers.ShouldMatchers {
  test("Simple queries") {
    parse("select name,age from person") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))

    parse("select name,age from person where age > 10") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))

    parse("select name,age from person where age = 10 order by age") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))

    parse("select name,age from person where age < 10 or age > 20 order by age asc") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))
  }

  test("Alias queries") {
    parse("select p.name,p.age from person as p") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))

    parse("select p.name, p.age from person as p where p.age > 10") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))

    parse("select p.name,p.age from person p where p.age < 10 order by p.age") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))

    parse("select p.name,p.age from person as p where p.age = 10 and p.name='joe' order by p.age asc") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("person", "age")))))
  }

  test("Variable queries") {
    parse("select name, person.age from person where age > ?") should 
      equal(Right(Select(List(Column("person", "age")), List(Column("person", "name"), Column("person", "age")))))

    parse("select name, person.age from person where person.age > ?") should 
      equal(Right(Select(List(Column("person", "age")), List(Column("person", "name"), Column("person", "age")))))

    parse("select p.name, p.age from person as p where p.age > ?") should 
      equal(Right(Select(List(Column("person", "age")), List(Column("person", "name"), Column("person", "age")))))

    parse("select p.name,p.age from person as p where p.age = ? and p.name=? order by p.age asc") should 
      equal(Right(Select(List(Column("person", "age"), Column("person", "name")), List(Column("person", "name"), Column("person", "age")))))
  }

  test("Equi joins") {
    parse("select p.name, c.age from person as p, child c where p.name > c.parent") should 
      equal(Right(Select(Nil, List(Column("person", "name"), Column("child", "age")))))

    parse("select p.name, c.age from person as p, child c where p.name > c.parent and c.id>?") should 
      equal(Right(Select(List(Column("child", "id")), List(Column("person", "name"), Column("child", "age")))))
  }
}