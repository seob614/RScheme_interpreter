# Scheme-Python Code Converter

This is the document that record every change We've made to our scheme project code to implement this converter. The project is inspired by the scheme project from **CS 61A** at UC Berkeley.

## Scheme -> Python

### Get Started

Run ``python scheme.py`` to get started. We will update README for any changes we have implemented. Beyond this, ``scheme.py`` perform as a Scheme interpreter.

Type in ``(exit)`` to terminate the program.

### Implementation

1. **Builtins**

	For example, for plus operation in scheme, if we type in the following: 

	```
	(+ 1 2)
	```

	The output should be:

	```
	1 + 2
	```

	Now we still want it to compute the result, but output the code in Python order instead of output the result of computation like in the interpreter.

	*Modification*

	In ``BuiltinProcedure.apply``, we change the output in ``try``. Instead of return ``self.fn``, we still do the evaluation but return the expression in a different order.

	Builtins we've implemented in our code:
	
		* Arithmatic:
			= + - * / < > <= >= expt abs

		* Boolean Operation:
			boolean? not eq? null? even? odd?

2. **Special Forms**

	Some of these special forms could be done similarly to the builtins procedure above, but consider we have other methods in ``scheme.py`` to deal with these special procdures, we will handle them in those methods.

	1. **Define**

		Define in Scheme can be used either name defining or procedure defining. In Python, they corresponds to assignment and method defining.

		For instance, if type in the following:

		```
		(define a 2)
		```

		It should output:

		```
		a = 2
		```

		For arithmatic expressions, it keeps the origin expression instead of evaluating it.

		```
		(define tau (* 3.14 2))
		```

		Output:

		```
		tau = (3.14 * 2)
		```

		Another use of Define is method defining. Say for input:

		```
		(define (f x) (* x 2))
		```

		It's defining a method ``f`` which takes in one argument ``x``. The output therefore should be:

		```
		def f(x):
			return (x * 2)
		```

		To implement this, we give ``scheme_eval`` a new optional argument ``from_define`` to avoid *unknown identifier* exception.

	2. **lambda**

		A ``lambda`` procedure is just like ``lambda`` in Python. For instance, with input:

		```
		(lambda (x y)(+ x y))
		```

		Output should also be a lambda expression:

		```
		lambda x, y: (x + y)
		```

