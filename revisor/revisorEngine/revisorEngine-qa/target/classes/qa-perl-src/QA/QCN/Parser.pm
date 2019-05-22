package QCN;

use Carp;

use Time::HiRes qw( gettimeofday );
use Digest::MD5 qw( md5_hex );

sub add_constraints_from_file {
    my ($self, $f) = @_;
    open my $h, '<', $f or croak 'Unable to open file to add constraints';
    while (<$h>) {
        chomp;
        next if m{ ^ \s* % }x;
        next unless m[ { ]x;
        my ( $v, $r, $w ) = m[ ^ \s* (.+?) \s* { \s* (.+?) \s* } \s* (.+?) \s* $ ]x;
        $self->add_constraint( $v, $w, $self->{'algebra'}->parse_relation( $r ) );
    }
    $self->update_tuples;
}

sub add_constraints_from_string {
    my ($self, $s) = @_;
    my @s = split /\s*;\s*/, $s;
    for (@s) {
        next if m{ ^ \s* % }x;
        next unless m[ { ]x;
        my ( $v, $r, $w ) = m[ ^ \s* (.+?) \s* { \s* (.+?) \s* } \s* (.+?) \s* $ ]x;
        # new 29.10.2013 for extended format
        if ( my ($s) = $r =~ m{ \s* not\( \s* (.+?) \s* \) \s* }x ) {
            $self->add_constraint_negate( $v, $w, $self->{'algebra'}->parse_relation( $s ) );
        }
        else {
            $self->add_constraint( $v, $w, $self->{'algebra'}->parse_relation( $r ) );
        }
    }
    $self->update_tuples;
}

sub pretty_print {
    my ($self) = @_;
    return $self->pretty_print_with_separator( "\n" ) . "\n";
}

sub compact_pretty_print {
    my ($self) = @_;
    return $self->pretty_print_with_separator( " ; " );
}

sub pretty_print_with_separator {
    my ($self, $sep) = @_;
    
    # il y a un moment où les tuples sont perdus sans l'option -h, mais
    # je n'arrive pas à trouver quand.
    $self->update_tuples;
    my @str;
    for my $pair (@{ $self->squares}) {
        my ($i,$j) = @$pair;
        push @str, $i . ' { ' . (join ', ', @{ $self->get_constraint( $i, $j ) } ) . ' } ' . $j;
    }
    return join $sep, @str;
}


# Génère un hachage à partir d'un RCQ
sub get_hash {
    my ($self) = @_;
    #return md5_hex( map { @{ $self->get_constraint( $_->[0], $_->[1] ) } } @{ $self->squares } );
    return md5_hex( $self );
}

# Génère un hachage à partir d'un RCQ, en principe égal pour deux RCQ identiques
sub get_hash_not_unique {
    my ($self) = @_;
    return 'md5' . md5_hex( map { @{ $self->get_constraint( $_->[0], $_->[1] ) } } @{ $self->squares } );
    #return md5_hex( $self );
}

sub to_dot {
    my ($self) = @_;
    #my ($schmouk, $id) = gettimeofday();
    my $id = $self->get_hash;
    my $dot = '';
    for my $i (@{$self->get_variables}) {
        $ci = $i;
        $ci =~ s/\W/_/g;
        $dot .= "v_".$id."_".$ci." [label=\"$i\"]\n";
    }
    for my $pair (@{$self->squares}) {
        my ($i, $j) = @$pair;
        $r = join ',', @{ $self->get_constraint( $i, $j ) };
        $i =~ s/\W/_/g;
        $j =~ s/\W/_/g;
        $dot .= "v_".$id."_".$i." -> v_".$id."_".$j. "[label=\"{$r}\"]\n";
    }
    return $dot;
}



# ces anciennes fonctions ne conviennent pas : elles n'impriment pas
# les relations non spécifiées !

#~ sub pretty_print {
    #~ my ($self) = @_;
    #~ my $str = '';
    #~ for my $i (keys %{ $self->{'constraints'} }) {
        #~ for my $j (keys %{ $self->{'constraints'}{$i} }) {
            #~ $str .= $i . ' { ' . (join ', ', @{$self->{'constraints'}{$i}{$j}} ) . ' } ' . $j . "\n";
        #~ }
    #~ }
    #~ return $str;
#~ }
#~ 
#~ sub compact_pretty_print {
    #~ my ($self) = @_;
    #~ my $str = '';
    #~ for my $i (keys %{ $self->{'constraints'} }) {
        #~ for my $j (keys %{ $self->{'constraints'}{$i} }) {
            #~ $str .= $i . ' { ' . (join ', ', @{$self->{'constraints'}{$i}{$j}} ) . ' } ' . $j . ' ; ';
        #~ }
    #~ }
    #~ return $str;
#~ }

1;
