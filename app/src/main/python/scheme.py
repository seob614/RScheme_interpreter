"""A Scheme interpreter and its read-eval-print loop."""
from __future__ import print_function  # Python 2 compatibility

from scheme_builtins import *
from scheme_reader import *
from ucb import main, trace

##############
# Eval/Apply #
##############


def scheme_eval(expr, env, _=None, from_define = False): # Optional third argument is ignored
    """Evaluate Scheme expression EXPR in environment ENV.

    >>> expr = read_line('(+ 2 2)')
    >>> expr
    Pair('+', Pair(2, Pair(2, nil)))
    >>> scheme_eval(expr, create_global_frame())
    4
    """
    # Evaluate atoms
    if scheme_symbolp(expr):
      if from_define:
        return expr
      else:
          return env.lookup(expr)
    elif self_evaluating(expr):
        return expr

    # All non-atomic expressions are lists (combinations)
    if not scheme_listp(expr):
        raise SchemeError('잘못된 형식의 리스트: {0}'.format(repl_str(expr))+'\nmalformed list: {0}'.format(repl_str(expr)))
    first, rest = expr.first, expr.rest
    if scheme_symbolp(first) and first in SPECIAL_FORMS:
        return SPECIAL_FORMS[first](rest, env)
    else:
        # BEGIN PROBLEM 4
        "*** YOUR CODE HERE ***"
        operator = scheme_eval(first, env)
        check_procedure(operator)
        if isinstance(operator, MacroProcedure):
            return scheme_eval(operator.apply_macro(rest, env), env)
        def one_arg_eval(exp):
              return scheme_eval(exp, env, from_define = from_define)
        operands = rest.map(one_arg_eval)
        return scheme_apply(operator, operands, env)
        # END PROBLEM 4

def self_evaluating(expr):
    """Return whether EXPR evaluates to itself."""
    return (scheme_atomp(expr) and not scheme_symbolp(expr)) or expr is None

def scheme_apply(procedure, args, env):
    """Apply Scheme PROCEDURE to argument values ARGS (a Scheme list) in
    environment ENV."""
    check_procedure(procedure)
    if isinstance(procedure, BuiltinProcedure):
        return procedure.apply(args, env)
    else:
        new_env = procedure.make_call_frame(args, env)
        return eval_all(procedure.body, new_env)

def eval_all(expressions, env):
    """Evaluate each expression in the Scheme list EXPRESSIONS in
    environment ENV and return the value of the last."""
    # BEGIN PROBLEM 7
    if expressions:
        while expressions.rest:
            scheme_eval(expressions.first, env)
            expressions = expressions.rest
        return scheme_eval(expressions.first, env, True)
    # END PROBLEM 7

################
# Environments #
################

class Frame(object):
    """An environment frame binds Scheme symbols to Scheme values."""

    def __init__(self, parent):
        """An empty frame with parent frame PARENT (which may be None)."""
        self.bindings = {}
        self.parent = parent

    def __repr__(self):
        if self.parent is None:
            return '<Global Frame>'
        s = sorted(['{0}: {1}'.format(k, v) for k, v in self.bindings.items()])
        return '<{{{0}}} -> {1}>'.format(', '.join(s), repr(self.parent))

    def define(self, symbol, value):
        """Define Scheme SYMBOL to have VALUE."""
        # BEGIN PROBLEM 2
        "*** YOUR CODE HERE ***"
        self.bindings[symbol] = value
        # END PROBLEM 2

    def lookup(self, symbol):
        """Return the value bound to SYMBOL. Errors if SYMBOL is not found."""
        # BEGIN PROBLEM 2
        "*** YOUR CODE HERE ***"
        if symbol in self.bindings.keys():
            return self.bindings[symbol]
        if self.parent:
            return self.parent.lookup(symbol)
        # END PROBLEM 2
        raise SchemeError('알 수 없는 식별자: {0}'.format(symbol)+'\nunknown identifier: {0}'.format(symbol))


    def make_child_frame(self, formals, vals):
        """Return a new local frame whose parent is SELF, in which the symbols
        in a Scheme list of formal parameters FORMALS are bound to the Scheme
        values in the Scheme list VALS. Raise an error if too many or too few
        vals are given.

        >>> env = create_global_frame()
        >>> formals, expressions = read_line('(a b c)'), read_line('(1 2 3)')
        >>> env.make_child_frame(formals, expressions)
        <{a: 1, b: 2, c: 3} -> <Global Frame>>
        """
        # BEGIN PROBLEM 10
        "*** YOUR CODE HERE ***"
        if len(formals) != len(vals):
            raise SchemeError
        else:
            new_frame = Frame(self)
            while formals and vals:
                new_frame.define(formals.first, vals.first)
                formals, vals = formals.rest, vals.rest
            return new_frame
        # END PROBLEM 10

##############
# Procedures #
##############

class Procedure(object):
    """The supertype of all Scheme procedures."""

def scheme_procedurep(x):
    return isinstance(x, Procedure)

class BuiltinProcedure(Procedure):
    """A Scheme procedure defined as a Python function."""

    def __init__(self, fn, use_env=False, name='builtin'):
        self.name = name
        self.fn = fn
        self.use_env = use_env

    def __str__(self):
        return '#[{0}]'.format(self.name)

    def apply(self, args, env):
        """Apply SELF to ARGS in ENV, where ARGS is a Scheme list.

        >>> env = create_global_frame()
        >>> plus = env.bindings['+']
        >>> twos = Pair(2, Pair(2, nil))
        >>> plus.apply(twos, env)
        4
        """
        if not scheme_listp(args):
            raise SchemeError('인수가 리스트에 없습니다: {0}'.format(args)+'\narguments are not in a list: {0}'.format(args))
        # Convert a Scheme list to a Python list
        python_args = []
        while args is not nil:
            python_args.append(args.first)
            args = args.rest
        # BEGIN PROBLEM 3
        "*** YOUR CODE HERE ***"
        if self.use_env:
            python_args.append(env)
        try:
            if len(python_args) > 1:

              x, y = python_args[0], python_args[1]

              if self.name == '=':
                return '(' + str(x) + ' == ' + str(y) + ')'

              elif self.name == 'eq?':
                if scheme_numberp(x) and scheme_numberp(y) or scheme_symbolp(x) and scheme_symbolp(y):
                  return '(' + str(x) + ' == ' + str(y) + ')'
                else:
                  return '(' + str(x) + ' is ' + str(y) + ')'

              elif self.name == 'equal?':
                return 'Sorry, currently unavaiable!'

              elif self.name == 'expt':
                return '(' + 'pow(' + str(x) + ', ' + str(y) + ')' + ')'

              ret = '('
              for i in range(len(python_args)):
                ret += str(python_args[i])
                if not i == len(python_args) - 1:
                  ret += ' ' + self.name + ' '
              return ret + ')'

            else: # hard code every one/none arg builtins :(

              _ = self.fn(*python_args)

              x = python_args[0]

              if self.name == 'boolean?':
                return '(' + str(x) + ' is True or ' + str(x) + ' is False' + ')'

              elif self.name == 'not':
                return '(' + 'not ' + str(x) + ')'

              elif self.name == 'null?':
                return '(' + str(x) + ')'

              elif self.name == 'abs':
                return '(' + 'abs(' + str(x) + ')' + ')'

              elif self.name == 'even?':
                return '(' + str(x) + ' % 2 == 0' + ')'

              elif self.name == 'odd?':
                return '(' + str(x) + ' % 2 == 1' + ')'

        except TypeError:
            raise SchemeError
        # END PROBLEM 3

class LambdaProcedure(Procedure):
    """A procedure defined by a lambda expression or a define form."""

    def __init__(self, formals, body, env):
        """A procedure with formal parameter list FORMALS (a Scheme list),
        whose body is the Scheme list BODY, and whose parent environment
        starts with Frame ENV."""
        self.formals = formals
        self.body = body
        self.env = env

    def make_call_frame(self, args, env):
        """Make a frame that binds my formal parameters to ARGS, a Scheme list
        of values, for a lexically-scoped call evaluated in environment ENV."""
        # BEGIN PROBLEM 11
        "*** YOUR CODE HERE ***"
        return self.env.make_child_frame(self.formals, args)
        # END PROBLEM 11

    def __str__(self):
        return str(Pair('lambda', Pair(self.formals, self.body)))

    def __repr__(self):
        return 'LambdaProcedure({0}, {1}, {2})'.format(
            repr(self.formals), repr(self.body), repr(self.env))

class MacroProcedure(LambdaProcedure):
    """A macro: a special form that operates on its unevaluated operands to
    create an expression that is evaluated in place of a call."""

    def apply_macro(self, operands, env):
        """Apply this macro to the operand expressions."""
        return complete_apply(self, operands, env)

def add_builtins(frame, funcs_and_names):
    """Enter bindings in FUNCS_AND_NAMES into FRAME, an environment frame,
    as built-in procedures. Each item in FUNCS_AND_NAMES has the form
    (NAME, PYTHON-FUNCTION, INTERNAL-NAME)."""
    for name, fn, proc_name in funcs_and_names:
        frame.define(name, BuiltinProcedure(fn, name=proc_name))

#################
# Special Forms #
#################

# Each of the following do_xxx_form functions takes the cdr of a special form as
# its first argument---a Scheme list representing a special form without the
# initial identifying symbol (if, lambda, quote, ...). Its second argument is
# the environment in which the form is to be evaluated.

def do_define_form(expressions, env):
    """Evaluate a define form."""
    check_form(expressions, 2)
    target = expressions.first
    if scheme_symbolp(target):
        check_form(expressions, 2, 2)
        # BEGIN PROBLEM 5
        "*** YOUR CODE HERE ***"
        val = scheme_eval(expressions.rest.first, env)
        env.define(target, val)
        return target + ' = ' + str(val)
        # END PROBLEM 5
    elif isinstance(target, Pair) and scheme_symbolp(target.first):
        # BEGIN PROBLEM 9
        "*** YOUR CODE HERE ***"
        formals = target.rest
        body = expressions.rest
        lamb_procedure = LambdaProcedure(formals, body, env)
        env.define(target.first, lamb_procedure)
        return 'def ' + str(target.first) + str(formals) + ':\n' + '\treturn ' + str(scheme_eval(body.first, env, from_define = True))
        # END PROBLEM 9
    else:
        bad_target = target.first if isinstance(target, Pair) else target
        raise SchemeError('심볼이 아닙니다: {0}'.format(bad_target)+'\nnon-symbol: {0}'.format(bad_target))

def do_quote_form(expressions, env):
    """Evaluate a quote form."""
    check_form(expressions, 1, 1)
    # BEGIN PROBLEM 6
    "*** YOUR CODE HERE ***"
    return expressions.first
    # END PROBLEM 6

def do_begin_form(expressions, env):
    """Evaluate a begin form."""
    check_form(expressions, 1)
    return eval_all(expressions, env)

def do_lambda_form(expressions, env):
    """Evaluate a lambda form."""
    check_form(expressions, 2)
    formals = expressions.first
    check_formals(formals)
    # BEGIN PROBLEM 8
    str_formals = str(formals)
    ret_formals = ''
    for i in range(1, len(str_formals) - 1):
        if not str_formals[i] == ' ':
            ret_formals += str_formals[i] + ', '
    body = expressions.rest
    lamb = LambdaProcedure(formals, body, env)
    res = 'lambda ' + ret_formals[:len(ret_formals) - 2] + ': ' + scheme_eval(body.first, env, from_define = True)
    return res
    # END PROBLEM 8

def do_if_form(expressions, env):
    """Evaluate an if form."""
    check_form(expressions, 2, 3)
    if scheme_truep(scheme_eval(expressions.first, env)):
        return scheme_eval(expressions.rest.first, env, True)
    elif len(expressions) == 3:
        return scheme_eval(expressions.rest.rest.first, env, True)

def do_and_form(expressions, env):
    """Evaluate a (short-circuited) and form."""
    # BEGIN PROBLEM 12
    if expressions == nil:
        return True
    if expressions.rest == nil:
        return scheme_eval(expressions.first, env, True)
    evaled1 = scheme_eval(expressions.first, env)
    if not scheme_truep(evaled1):
        return evaled1
    return do_and_form(expressions.rest, env)
    # END PROBLEM 12

def do_or_form(expressions, env):
    """Evaluate a (short-circuited) or form."""
    # BEGIN PROBLEM 12
    if expressions == nil:
        return False
    if expressions.rest == nil:
        return scheme_eval(expressions.first, env, True)
    evaled2 = scheme_eval(expressions.first, env)
    if scheme_truep(evaled2):
        return evaled2
    return do_or_form(expressions.rest, env)

def do_cond_form(expressions, env):
    """Evaluate a cond form."""
    while expressions is not nil:
        clause = expressions.first
        check_form(clause, 1)
        if clause.first == 'else':
            test = True
            if expressions.rest != nil:
                raise SchemeError('else는 마지막에 와야 합니다.'+'\nelse must be last')
        else:
            test = scheme_eval(clause.first, env)
        if scheme_truep(test):
            # BEGIN PROBLEM 13
            "*** YOUR CODE HERE ***"
            if clause.rest:
                return eval_all(clause.rest, env)
            return test
            # END PROBLEM 13
        expressions = expressions.rest

def do_let_form(expressions, env):
    """Evaluate a let form."""
    check_form(expressions, 2)
    let_env = make_let_frame(expressions.first, env)
    return eval_all(expressions.rest, let_env)

def make_let_frame(bindings, env):
    """Create a child frame of ENV that contains the definitions given in
    BINDINGS. The Scheme list BINDINGS must have the form of a proper bindings
    list in a let expression: each item must be a list containing a symbol
    and a Scheme expression."""
    if not scheme_listp(bindings):
        raise SchemeError('let 표현식에서 잘못된 바인딩 리스트.'+'\nbad bindings list in let form')
    # BEGIN PROBLEM 14
    "*** YOUR CODE HERE ***"
    formals, vals = nil, nil
    while bindings:
        pair = bindings.first
        check_form(pair, 2, 2)
        formal, val = pair.first, pair.rest.first
        val = scheme_eval(val, env)
        formals = Pair(formal, formals)
        check_formals(formals)
        vals = Pair(val, vals)
        bindings = bindings.rest
    return env.make_child_frame(formals, vals)
    # END PROBLEM 14

def do_define_macro(expressions, env):
    """Evaluate a define-macro form."""
    # BEGIN Problem 20
    "*** YOUR CODE HERE ***"
    check_form(expressions, 2)
    target = expressions.first
    name = target.first
    formals = target.rest
    body = expressions.rest.first
    env.define(name, MacroProcedure(formals, body, env))
    return name
    # END Problem 20


def do_quasiquote_form(expressions, env):
    """Evaluate a quasiquote form with parameters EXPRESSIONS in
    environment ENV."""
    def quasiquote_item(val, env, level):
        """Evaluate Scheme expression VAL that is nested at depth LEVEL in
        a quasiquote form in environment ENV."""
        if not scheme_pairp(val):
            return val
        if val.first == 'unquote':
            level -= 1
            if level == 0:
                expressions = val.rest
                check_form(expressions, 1, 1)
                return scheme_eval(expressions.first, env)
        elif val.first == 'quasiquote':
            level += 1

        return val.map(lambda elem: quasiquote_item(elem, env, level))

    check_form(expressions, 1, 1)
    return quasiquote_item(expressions.first, env, 1)

def do_unquote(expressions, env):
    raise SchemeError('윗 쉼표에서 벗어난 반점입니다.'+'\nunquote outside of quasiquote')

def do_display_form(expressions, env):
    """Evaluate a display form."""
    check_form(expressions, 1, 1)
    target = expressions.first
    # 표현식이 함수인 경우
    if isinstance(target, Pair) and target.rest is not nil:
        function_result = display_function(target, env)
        return 'print({0})'.format(function_result)
    else:
        return 'print({0})'.format(repl_str(target))

def display_function(target, env):
    function_name = target.first
    argument_strings = generate_argument_strings(target.rest, env)
    return '{0}({1})'.format(repl_str(function_name), argument_strings)

def generate_argument_strings(arguments, env):
    argument_strings = ''
    while isinstance(arguments, Pair) and arguments is not nil:
        get_first = arguments.first
        
        if isinstance(get_first, Pair):
            function_result = display_function(get_first, env)
            argument_strings += repl_str(function_result) + ', '
        else:
            argument_strings += repl_str(get_first) + ', '
            
        arguments = arguments.rest
        
    return argument_strings[:-2] # 마지막에 추가된 ", " 제거

def do_newline_form(expressions, env):
    """Evaluate a display form."""
    check_form(expressions, 0, 0)
    return 'print()'

"""display 명령어 추가"""
SPECIAL_FORMS = {
    'and': do_and_form,
    'begin': do_begin_form,
    'cond': do_cond_form,
    'define': do_define_form,
    'if': do_if_form,
    'lambda': do_lambda_form,
    'let': do_let_form,
    'or': do_or_form,
    'quote': do_quote_form,
    'define-macro': do_define_macro,
    'quasiquote': do_quasiquote_form,
    'unquote': do_unquote,
    'display': do_display_form,
    'newline': do_newline_form,
}

# Utility methods for checking the structure of Scheme programs

def check_form(expr, min, max=float('inf')):
    """Check EXPR is a proper list whose length is at least MIN and no more
    than MAX (default: no maximum). Raises a SchemeError if this is not the
    case.

    >>> check_form(read_line('(a b)'), 2)
    """
    if not scheme_listp(expr):
        raise SchemeError('잘못된 표현식: '+ repl_str(expr) +'\nbadly formed expression: ' + repl_str(expr))
    length = len(expr)
    if length < min:
        raise SchemeError('표현식 안에 인자가 너무 적습니다.' +'\ntoo few operands in form')
    elif length > max:
        raise SchemeError('표현식 안에 인자가 너무 많습니다.' +'\ntoo many operands in form')

def check_formals(formals):
    """Check that FORMALS is a valid parameter list, a Scheme list of symbols
    in which each symbol is distinct. Raise a SchemeError if the list of
    formals is not a list of symbols or if any symbol is repeated.

    >>> check_formals(read_line('(a b c)'))
    """
    symbols = set()
    def check_and_add(symbol, is_last):
        if not scheme_symbolp(symbol):
            raise SchemeError('심볼이 아닙니다: {0}'.format(symbol) +'\nnon-symbol: {0}'.format(symbol))
        if symbol in symbols:
            raise SchemeError('중복된 심볼: {0}'.format(symbol) +'\nduplicate symbol: {0}'.format(symbol))
        symbols.add(symbol)

    while isinstance(formals, Pair):
        check_and_add(formals.first, formals.rest is nil)
        formals = formals.rest


def check_procedure(procedure):
    """Check that PROCEDURE is a valid Scheme procedure."""
    if not scheme_procedurep(procedure):
        raise SchemeError('{0}는 호출 가능하지 않습니다: {1}'.format(type(procedure).__name__.lower(), repl_str(procedure))
                          +'\n{0} is not callable: {1}'.format(type(procedure).__name__.lower(), repl_str(procedure)))

#################
# Dynamic Scope #
#################

class MuProcedure(Procedure):

    def __init__(self, formals, body):
        """A procedure with formal parameter list FORMALS (a Scheme list) and
        Scheme list BODY as its definition."""
        self.formals = formals
        self.body = body

    # BEGIN PROBLEM 15
    "*** YOUR CODE HERE ***"

    def make_call_frame(self, args, env):
        return env.make_child_frame(self.formals, args)
    # END PROBLEM 15

    def __str__(self):
        return str(Pair('mu', Pair(self.formals, self.body)))

    def __repr__(self):
        return 'MuProcedure({0}, {1})'.format(
            repr(self.formals), repr(self.body))

def do_mu_form(expressions, env):
    """Evaluate a mu form."""
    check_form(expressions, 2)
    formals = expressions.first
    check_formals(formals)
    # BEGIN PROBLEM 15
    "*** YOUR CODE HERE ***"
    mu_procedure = MuProcedure(formals, expressions.rest)
    return mu_procedure
    # END PROBLEM 15

SPECIAL_FORMS['mu'] = do_mu_form

###########
# Streams #
###########

class Promise(object):
    """A promise."""
    def __init__(self, expression, env):
        self.expression = expression
        self.env = env

    def evaluate(self):
        if self.expression is not None:
            value = scheme_eval(self.expression, self.env)
            if not (value is nil or isinstance(value, Pair)):
                raise SchemeError("Promise를 강제로 실행한 결과는 쌍(pair)이거나 nil이어야 합니다. 잘못된 부분:%s"+"\nresult of forcing a promise should be a pair or nil, but was %s" % value)
            self.value = value
            self.expression = None
        return self.value

    def __str__(self):
        return '#[promise ({0}forced)]'.format(
                'not ' if self.expression is not None else '')

def do_delay_form(expressions, env):
    """Evaluates a delay form."""
    check_form(expressions, 1, 1)
    return Promise(expressions.first, env)

def do_cons_stream_form(expressions, env):
    """Evaluate a cons-stream form."""
    check_form(expressions, 2, 2)
    return Pair(scheme_eval(expressions.first, env),
                do_delay_form(expressions.rest, env))

SPECIAL_FORMS['cons-stream'] = do_cons_stream_form
SPECIAL_FORMS['delay'] = do_delay_form

##################
# Tail Recursion #
##################

class Thunk(object):
    """An expression EXPR to be evaluated in environment ENV."""
    def __init__(self, expr, env):
        self.expr = expr
        self.env = env

def complete_apply(procedure, args, env):
    """Apply procedure to args in env; ensure the result is not a Thunk."""
    val = scheme_apply(procedure, args, env)
    if isinstance(val, Thunk):
        return scheme_eval(val.expr, val.env)
    else:
        return val

def optimize_tail_calls(original_scheme_eval):
    """Return a properly tail recursive version of an eval function."""
    def optimized_eval(expr, env, tail=False, from_define=False):
        """Evaluate Scheme expression EXPR in environment ENV. If TAIL,
        return a Thunk containing an expression for further evaluation.
        """
        if tail and not scheme_symbolp(expr) and not self_evaluating(expr):
            return Thunk(expr, env)

        result = Thunk(expr, env)
        # BEGIN
        "*** YOUR CODE HERE ***"
        while isinstance(result, Thunk):
            result = original_scheme_eval(result.expr, result.env)
        return result
        # END
    return optimized_eval






################################################################
# Uncomment the following line to apply tail call optimization #
################################################################
# scheme_eval = optimize_tail_calls(scheme_eval)






####################
# Extra Procedures #
####################

def scheme_map(fn, s, env):
    check_type(fn, scheme_procedurep, 0, 'map')
    check_type(s, scheme_listp, 1, 'map')
    return s.map(lambda x: complete_apply(fn, Pair(x, nil), env))

def scheme_filter(fn, s, env):
    check_type(fn, scheme_procedurep, 0, 'filter')
    check_type(s, scheme_listp, 1, 'filter')
    head, current = nil, nil
    while s is not nil:
        item, s = s.first, s.rest
        if complete_apply(fn, Pair(item, nil), env):
            if head is nil:
                head = Pair(item, nil)
                current = head
            else:
                current.rest = Pair(item, nil)
                current = current.rest
    return head

def scheme_reduce(fn, s, env):
    check_type(fn, scheme_procedurep, 0, 'reduce')
    check_type(s, lambda x: x is not nil, 1, 'reduce')
    check_type(s, scheme_listp, 1, 'reduce')
    value, s = s.first, s.rest
    while s is not nil:
        value = complete_apply(fn, scheme_list(value, s.first), env)
        s = s.rest
    return value

################
# Input/Output #
################

def list_to_string_with_newline(lst):
    result = "\n".join(lst)
    return result

def read_eval_print_loop(next_line, env, interactive=False, quiet=False,
                         startup=False, load_files=(), report_errors=False):
    """Read and evaluate input until an end of file or keyboard interrupt."""
    all_result = []
    
    if startup:
        for filename in load_files:
            scheme_load(filename, True, env)
    try:
        src = next_line()
        while src.more_on_line:
            expression = scheme_read(src)
            result = scheme_eval(expression, env)
            if not quiet and result is not None:
                print(repl_str(result))
                all_result.append(repl_str(result))
        return list_to_string_with_newline(all_result)

    except (SchemeError, SyntaxError, ValueError, RuntimeError) as err:
        if report_errors:
            if isinstance(err, SyntaxError):
                err = SchemeError(err)
                raise err
                return err
        if (isinstance(err, RuntimeError) and
            'maximum recursion depth exceeded' not in getattr(err, 'args')[0]):
            raise
        elif isinstance(err, RuntimeError):
            print('Error: maximum recursion depth exceeded')
            return 'Error: maximum recursion depth exceeded'
        else:
            print('Error:', err)
            result = "Error:"+str(err)
            return result
    except KeyboardInterrupt:  # <Control>-C
        if not startup:
            raise
        print()
        print('KeyboardInterrupt')
        if not interactive:
            return
    except EOFError:  # <Control>-D, etc.
        print()
        return
        

def scheme_load(*args):
    """Load a Scheme source file. ARGS should be of the form (SYM, ENV) or
    (SYM, QUIET, ENV). The file named SYM is loaded into environment ENV,
    with verbosity determined by QUIET (default true)."""
    if not (2 <= len(args) <= 3):
        expressions = args[:-1]
        raise SchemeError('올바르지 않은 인수 개수로 "load"가 제공되었습니다: ''{0}'.format(len(expressions))
                          +'\n"load" given incorrect number of arguments: ''{0}'.format(len(expressions)))
    sym = args[0]
    quiet = args[1] if len(args) > 2 else True
    env = args[-1]
    if (scheme_stringp(sym)):
        sym = eval(sym)
    check_type(sym, scheme_symbolp, 0, 'load')
    with scheme_open(sym) as infile:
        lines = infile.readlines()
    args = (lines, None) if quiet else (lines,)
    def next_line():
        return buffer_lines(*args)

    read_eval_print_loop(next_line, env, quiet=quiet, report_errors=True)

def scheme_load_all(directory, env):
    """
    Loads all .scm files in the given directory, alphabetically. Used only
        in tests/ code.
    """
    assert scheme_stringp(directory)
    directory = eval(directory)
    import os
    for x in sorted(os.listdir(".")):
        if not x.endswith(".scm"):
            continue
        scheme_load(x, env)

def scheme_open(filename):
    """If either FILENAME or FILENAME.scm is the name of a valid file,
    return a Python file opened to it. Otherwise, raise an error."""
    try:
        return open(filename)
    except IOError as exc:
        if filename.endswith('.scm'):
            raise SchemeError(str(exc))
    try:
        return open(filename + '.scm')
    except IOError as exc:
        raise SchemeError(str(exc))

def create_global_frame():
    """Initialize and return a single-frame environment with built-in names."""
    env = Frame(None)
    env.define('eval',
               BuiltinProcedure(scheme_eval, True, 'eval'))
    env.define('apply',
               BuiltinProcedure(complete_apply, True, 'apply'))
    env.define('load',
               BuiltinProcedure(scheme_load, True, 'load'))
    env.define('load-all',
               BuiltinProcedure(scheme_load_all, True, 'load-all'))
    env.define('procedure?',
               BuiltinProcedure(scheme_procedurep, False, 'procedure?'))
    env.define('map',
               BuiltinProcedure(scheme_map, True, 'map'))
    env.define('filter',
               BuiltinProcedure(scheme_filter, True, 'filter'))
    env.define('reduce',
               BuiltinProcedure(scheme_reduce, True, 'reduce'))
    env.define('undefined', None)
    add_builtins(env, BUILTINS)
    return env

def run(input_str):
    
    import argparse
    parser = argparse.ArgumentParser(description='CS 61A Scheme Interpreter')
    parser.add_argument('-load', '-i', action='store_true',
                       help='run file interactively')
    parser.add_argument('file', nargs='?',
                        type=argparse.FileType('r'), default=None,
                        help='Scheme file to run')
    args = parser.parse_args()

    """next_line = lambda: buffer_lines(get_str)"""

    """next_line = buffer_input"""

    next_line = lambda: buffer_lines([input_str])

    
    interactive = False
    load_files = []
    

    if args.file is not None:
        if args.load:
            load_files.append(getattr(args.file, 'name'))
        else:
            lines = args.file.readlines()
            def next_line():
                return buffer_lines(lines)
            interactive = False
    
    return str(read_eval_print_loop(next_line, create_global_frame(), startup=True,
                         interactive=interactive, load_files=load_files))
    tscheme_exitonclick()

"""
def start(input_str):
    if __name__ == "__main__":
        run(input_str)

start("(+ 1 1)(+ 2 2)(define a 10)")"""
