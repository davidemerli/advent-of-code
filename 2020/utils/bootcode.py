class BootcodeMachine:
    def __init__(self, program: list):
        self.program_counter = 0
        self.accumulator = 0
        self.instructions = program[:]
        self.exit_code = None

    def has_halted():
        return self.exit_code != None

    def acc(self, value: int):
        self.accumulator += value 
        return self.program_counter + 1

    def jmp(self, value: int):
        return self.program_counter + value

    def nop(self, value: int):
        return self.program_counter + 1

    def next(self):
        '''
        Attempts to perform an instruction

        returns 0 if the program has finished correctly
                1 if the program has finished unexpectedly
                None otherwise
        '''
        
        if self.program_counter == len(self.instructions):
            return 0
        elif self.program_counter > len(self.instructions):
            return 1
        
        op_code, arg = self.instructions[self.program_counter].split(' ')

        try: 
            self.program_counter = eval(f'self.{op_code}({arg})')
        except Exception:
            return 1

    def execute(self, detect_loop=False):
        '''
        Runs the program until it returns an integer 'exit_code'

        If 'detect_loop' is True, once the program tries to execute an instruction at
        a 'program_counter' that has already been visited, it returns with an 'exit_code' '2'
        '''

        indices = set()

        while True:
            pc = self.program_counter

            if pc in indices and detect_loop:
                return 2

            indices.add(pc)

            code = self.next()
            
            if code != None:
                self.exit_code

                return code